// @flow

import { Colors, Drawer, FormGroup } from '@blueprintjs/core'
import TaskList from './TaskList'
import ProcessChart, { chartNodeFromProcessedNode } from './ProcessChart'
import React from 'react'
import { calcGraph } from '../graph-utils'
import onOpenRemoveOverlayClass from '../../common/onOpenRemoveOverlayClass'
import TaskTemplateEditor from './TaskTemplateEditor'
import type { UserRoleType } from '../../datatypes/User'
import AppToaster from '../../app/AppToaster'
import type { IncompleteTaskTemplateType } from '../ProcessTemplateEditorTypes'

type PropsType = {
  userRoles: Map<number, UserRoleType>,
  tasks: IncompleteTaskTemplateType[],
  highlightValidation: boolean,
  onTasksChange: (tasks: IncompleteTaskTemplateType[]) => void,
  setDrawerOpened: (drawerOpened: boolean) => void
}

type StateType = {
  selectedTaskId: ?number,
  highlightTaskValidation: boolean
}

const taskValid = (task: IncompleteTaskTemplateType) =>
  !!task.name && task.responsibleUserRoleId !== null && !!task.estimatedDuration && task.estimatedDuration > 0

class TaskTemplateListEditor extends React.Component<PropsType, StateType> {
  state = {
    selectedTaskId: null,
    highlightTaskValidation: false
  }

  createTask = () => {
    if (this.unselectTask()) {
      const { tasks, onTasksChange } = this.props
      const newId = Math.max(...tasks.map(node => node.id), -1) + 1
      const newTask: IncompleteTaskTemplateType = {
        id: newId,
        predecessors: [],
        name: '',
        estimatedDuration: null,
        description: '',
        responsibleUserRoleId: null,
        necessaryClosings: 1
      }
      onTasksChange([...tasks, newTask])
      this.setState({ selectedTaskId: newId })
      this.props.setDrawerOpened(true)
    }
  }

  selectTaskId = (id: number) => {
    if (this.unselectTask()) {
      this.setState({ selectedTaskId: id })
      this.props.setDrawerOpened(true)
    }
  }

  unselectTask = (): boolean => {
    const task = this.props.tasks.find(task => task.id === this.state.selectedTaskId)
    if (task && !taskValid(task)) {
      AppToaster.show({
        icon: 'error',
        message: 'Please fill in all required values for the task template.',
        intent: 'danger'
      })
      this.setState({ highlightTaskValidation: true })
      return false
    }
    this.props.setDrawerOpened(false)
    this.setState({
      selectedTaskId: null,
      highlightTaskValidation: false
    })
    return true
  }

  onDeleteTask = () => {
    const { tasks, onTasksChange, setDrawerOpened } = this.props
    const { selectedTaskId } = this.state
    this.setState({
      selectedTaskId: null,
      highlightTaskValidation: false
    })
    onTasksChange(tasks
      .filter(task => task.id !== selectedTaskId)
      .map(task => ({
        ...task,
        predecessors: task.predecessors.filter(id => id !== selectedTaskId)
      })))
    setDrawerOpened(false)
  }

  taskChanged = (task: IncompleteTaskTemplateType) => this.props.onTasksChange(
    this.props.tasks.map(_task => _task.id === task.id ? task : _task)
  )

  render () {
    const { highlightValidation, tasks, userRoles } = this.props
    const { selectedTaskId, highlightTaskValidation } = this.state

    const task = tasks.find(task => task.id === selectedTaskId)
    const processedNodes = calcGraph(tasks)
    const chartNodes = processedNodes.map(
      node => chartNodeFromProcessedNode(node, node.id === selectedTaskId ? Colors.BLUE4 : Colors.BLUE1)
    )
    return <FormGroup label='Task Templates' labelInfo='(at least one required)'>
      <div style={{
        display: 'flex',
        borderRadius: '3px',
        padding: '10px',
        border: `1px solid ${highlightValidation && tasks.length === 0 ? Colors.RED2 : Colors.LIGHT_GRAY1}`
      }}>
        <TaskList selectedId={selectedTaskId} taskTemplates={processedNodes.map(node => node.data)}
                  createTask={this.createTask} selectTaskId={this.selectTaskId}
                  highlightAdd={highlightValidation && tasks.length === 0}/>
        <ProcessChart tasks={chartNodes} selectedId={selectedTaskId} selectTaskId={this.selectTaskId}/>
      </div>
      <Drawer size={Drawer.SIZE_SMALL} hasBackdrop={false} isOpen={task != null} title={task?.name || ''}
              onClose={this.unselectTask} style={{ overflow: 'auto' }} onOpening={onOpenRemoveOverlayClass}>
        {task &&
        <TaskTemplateEditor task={task} onChange={this.taskChanged} allTasks={tasks} onDelete={this.onDeleteTask}
                            userRoles={userRoles} highlightValidation={highlightTaskValidation}/>}
      </Drawer>
    </FormGroup>
  }
}

export default TaskTemplateListEditor
