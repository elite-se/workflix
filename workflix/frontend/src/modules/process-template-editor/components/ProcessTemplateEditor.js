// @flow

import React from 'react'
import ProcessChart from './ProcessChart'
import { Alert, Button, Colors, Drawer, FormGroup, H2 } from '@blueprintjs/core'
import TaskList from './TaskList'
import TaskTemplateEditor from './TaskTemplateEditor'
import type { UserRoleType, UserType } from '../../datatypes/User'
import ProcessDetailsEditor from './ProcessDetailsEditor'
import { calcGraph } from '../graph-utils'
import type { FilledProcessTemplateType } from '../../api/ProcessApi'
import AppToaster from '../../app/AppToaster'
import onOpenRemoveOverlayClass from '../../common/onOpenRemoveOverlayClass'

export type IncompleteTaskTemplateType = {|
  id: number,
  name: string,
  description: string,
  estimatedDuration: ?number,
  necessaryClosings: number,
  responsibleUserRoleId: ?number,
  predecessors: number[]
|}

export type IncompleteProcessTemplateType = {|
  tasks: IncompleteTaskTemplateType[],
  title: string,
  description: string,
  durationLimit: ?number,
  owner: ?UserType
|}

type PropsType = {
  users: Map<string, UserType>,
  userRoles: Map<number, UserRoleType>,
  title: string,
  initialProcessTemplate: IncompleteProcessTemplateType,
  onSave: FilledProcessTemplateType => Promise<void>
}

type StateType = {|
  ...IncompleteProcessTemplateType,
  selectedTaskId: ?number,
  highlightValidation: boolean,
  highlightTaskValidation: boolean,
  errorAlert: ?string,
  saveLoading: boolean
|}

const taskValid = (task: IncompleteTaskTemplateType) =>
  !!task.name && task.responsibleUserRoleId !== null && !!task.estimatedDuration && task.estimatedDuration > 0

class ProcessTemplateEditor extends React.Component<PropsType, StateType> {
  state = {
    ...this.props.initialProcessTemplate,
    selectedTaskId: null,
    highlightValidation: false,
    highlightTaskValidation: false,
    errorAlert: null,
    saveLoading: false
  }

  selectTaskId = (id: number) => {
    this.setState(state => {
      const task = state.tasks.find(task => task.id === state.selectedTaskId)
      if (task && !taskValid(task)) {
        AppToaster.show({
          icon: 'error',
          message: 'Please fill in all required values for the task template.',
          intent: 'danger'
        })
        return { highlightTaskValidation: true }
      }
      return {
        selectedTaskId: id,
        highlightTaskValidation: false
      }
    })
  }

  taskChanged = (task: IncompleteTaskTemplateType) => {
    this.setState(state => ({
      tasks: state.tasks.map(_task => _task.id === task.id ? task : _task)
    }))
  }

  createTask = () => {
    this.setState(state => {
      const newId = Math.max(...state.tasks.map(node => node.id), -1) + 1
      const newTask: IncompleteTaskTemplateType = {
        id: newId,
        predecessors: [],
        name: '',
        estimatedDuration: null,
        description: '',
        responsibleUserRoleId: null,
        necessaryClosings: 1
      }
      return {
        tasks: [...state.tasks, newTask],
        selectedTaskId: newId
      }
    })
  }

  unselectTask = () => this.setState(state => {
    const task = state.tasks.find(task => task.id === state.selectedTaskId)
    if (task && !taskValid(task)) {
      AppToaster.show({
        icon: 'error',
        message: 'Please fill in all required values for the task template.',
        intent: 'danger'
      })
      return { highlightTaskValidation: true }
    }
    return {
      selectedTaskId: null,
      highlightTaskValidation: false
    }
  })

  onDeleteTask = () => {
    this.setState(state => ({
      tasks: state.tasks
        .filter(task => task.id !== state.selectedTaskId)
        .map(task => ({
          ...task,
          predecessors: task.predecessors.filter(id => id !== state.selectedTaskId)
        })),
      selectedTaskId: null,
      highlightTaskValidation: false
    }))
  }

  onTitleChange = (title: string) => this.setState({ title })
  onDescriptionChange = (description: string) => this.setState({ description })
  onDurationLimitChange = (durationLimit: number) => this.setState({
    durationLimit: durationLimit > 0 ? durationLimit : null
  })

  onOwnerChange = (owner: ?UserType) => this.setState({ owner })

  onSaveClick = async () => {
    const { title, description, durationLimit, owner, tasks } = this.state
    if (!title || !durationLimit || !owner || tasks.length === 0) {
      AppToaster.show({
        icon: 'error',
        message: 'Please fill in all required values.',
        intent: 'danger'
      })
      return this.setState({ highlightValidation: true })
    }
    try {
      this.setState({ saveLoading: true })
      await this.props.onSave({
        title,
        description,
        durationLimit,
        ownerId: owner.id,
        taskTemplates: tasks.map(task => {
          if (!task.responsibleUserRoleId || !task.estimatedDuration) {
            throw new Error(`Task ${task.name} is invalid.`)
          }
          return {
            id: task.id,
            responsibleUserRoleId: task.responsibleUserRoleId,
            name: task.name,
            description: task.description,
            estimatedDuration: task.estimatedDuration || 0,
            necessaryClosings: task.necessaryClosings,
            predecessors: task.predecessors
          }
        })
      })
      this.setState({ saveLoading: false })
    } catch (e) {
      this.setState({
        errorAlert: e.message,
        saveLoading: false
      })
    }
  }

  closeAlert = () => this.setState({ errorAlert: null })

  render () {
    const {
      tasks, title, description, durationLimit, owner, saveLoading, errorAlert,
      selectedTaskId, highlightValidation, highlightTaskValidation
    } = this.state
    const { users, userRoles } = this.props
    const task = tasks.find(task => task.id === selectedTaskId)
    const processedNodes = calcGraph(tasks)
    return <div style={{
      flex: 1,
      display: 'flex',
      flexDirection: 'column',
      marginRight: selectedTaskId !== null ? Drawer.SIZE_SMALL : '0',
      transition: 'margin-right 0.3s'
    }}>
      <div style={{
        display: 'flex',
        flowDirection: 'row',
        marginBottom: '10px',
        justifyContent: 'start',
        alignItems: 'center'
      }}>
        <Button icon='floppy-disk' text='Save' intent='primary' loading={saveLoading} onClick={this.onSaveClick}/>
        <H2 style={{
          display: 'inline',
          marginLeft: '40px'
        }}>{this.props.title}</H2>
      </div>
      <ProcessDetailsEditor durationLimit={durationLimit} onDurationLimitChange={this.onDurationLimitChange}
                            onDescriptionChange={this.onDescriptionChange} description={description}
                            onTitleChange={this.onTitleChange} title={title} highlightValidation={highlightValidation}
                            users={users} owner={owner} onOwnerChange={this.onOwnerChange}/>
      <FormGroup label='Task Templates' labelInfo='(at least one required)'>
        <div style={{
          display: 'flex',
          borderRadius: '3px',
          padding: '10px',
          border: `1px solid ${highlightValidation && tasks.length === 0 ? Colors.RED2 : Colors.LIGHT_GRAY1}`
        }}>
          <TaskList selectedId={selectedTaskId} taskTemplates={processedNodes.map(node => node.data)}
                    createTask={this.createTask} selectTaskId={this.selectTaskId}
                    highlightAdd={highlightValidation && tasks.length === 0}/>
          <ProcessChart tasks={processedNodes}/>
        </div>
      </FormGroup>
      <Drawer size={Drawer.SIZE_SMALL} hasBackdrop={false} isOpen={task != null} title={task?.name || ''}
              onClose={this.unselectTask} style={{ overflow: 'auto' }} onOpening={onOpenRemoveOverlayClass}>
        {task &&
        <TaskTemplateEditor task={task} onChange={this.taskChanged} allTasks={tasks} onDelete={this.onDeleteTask}
                            userRoles={userRoles} highlightValidation={highlightTaskValidation}/>}
      </Drawer>
      <Alert isOpen={!!errorAlert} intent='danger' icon='error' confirmButtonText='Ok' onClose={this.closeAlert}>
        {errorAlert}
      </Alert>
    </div>
  }
}

export default ProcessTemplateEditor
