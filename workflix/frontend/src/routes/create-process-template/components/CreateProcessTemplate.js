// @flow

import React from 'react'
import ProcessChart from './ProcessChart'
import { times } from 'lodash'
import { Button, Drawer, H2, InputGroup, Label } from '@blueprintjs/core'
import TaskList from './TaskList'
import MOCK_TASK_TEMPLATES from './mockTasks'
import TaskTemplateEditor from './TaskTemplateEditor'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'
import AutoSizeTextArea from '../../../modules/common/AutoSizeTextArea'
import type { UserType } from '../../../modules/datatypes/User'
import UserApi from '../../../modules/api/UsersApi'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import UserSelect from './UserSelect'

export type ProcessedTaskTemplateType = {|
  ...TaskTemplateType,
  startDate: number, /* earliest possible start date */
  endDate: number /* earliest possible end date */
|}

const calcEndDates = (nodes: Array<TaskTemplateType>): Array<ProcessedTaskTemplateType> => {
  const leveledNodes = nodes.map(node => ({
    ...node,
    startDate: 0,
    endDate: node.estimatedDuration
  }))

  times(leveledNodes.length - 1, () => { // Bellman-Ford
    for (const node of leveledNodes) {
      node.startDate = Math.max(
        0,
        ...(node.predecessors.map(id => leveledNodes.find(x => x.id === id)?.endDate || 0))
      )
      node.endDate = node.startDate + node.estimatedDuration
    }
  })

  return leveledNodes.sort((node1, node2) => node1.startDate - node2.startDate)
}

type StateType = {
  tasks: Array<TaskTemplateType>,
  selectedTaskId: ?number,
  title: string,
  description: string,
  durationLimit: ?number,
  owner: ?UserType
}

type PropsType = {
  users: Map<string, UserType>
}

class CreateProcessTemplate extends React.Component<PropsType, StateType> {
  state = {
    tasks: MOCK_TASK_TEMPLATES,
    selectedTaskId: null,
    durationLimit: null,
    description: '',
    title: '',
    owner: null
  }

  selectTaskId = (id: number) => {
    this.setState({ selectedTaskId: id })
  }

  taskChanged = (task: TaskTemplateType) => {
    this.setState(state => ({
      tasks: state.tasks.map(_task => _task.id === task.id ? task : _task)
    }))
  }

  createTask = () => {
    this.setState(state => {
      const newId = Math.max(...state.tasks.map(node => node.id), -1) + 1
      const newTask: TaskTemplateType = {
        id: newId,
        predecessors: [],
        name: '',
        estimatedDuration: 1,
        description: '',
        necessaryClosings: 0
      }
      return {
        tasks: [...state.tasks, newTask],
        selectedTaskId: newId
      }
    })
  }

  unselectTask = () => this.setState({ selectedTaskId: null })

  onDeleteTask = () => {
    this.setState(state => ({
      tasks: state.tasks
        .filter(task => task.id !== state.selectedTaskId)
        .map(task => ({
          ...task,
          predecessors: task.predecessors.filter(id => id !== state.selectedTaskId)
        })),
      selectedTaskId: null
    }))
  }

  renderTaskTemplateEditor (): React$Node {
    const { tasks, selectedTaskId } = this.state
    const task = tasks.find(task => task.id === selectedTaskId)
    return <Drawer
      size={Drawer.SIZE_SMALL}
      hasBackdrop={false}
      isOpen={task != null}
      title={task?.name || ''}
      onClose={this.unselectTask}
      style={{ overflow: 'auto' }}>
      {task &&
      <TaskTemplateEditor task={task} onChange={this.taskChanged} allTasks={tasks} onDelete={this.onDeleteTask}/>}
    </Drawer>
  }

  onTitleChange = (event: SyntheticInputEvent<HTMLInputElement>) => this.setState(
    { title: event.target.value }
  )

  onDescriptionChange = (event: SyntheticInputEvent<HTMLInputElement>) => this.setState(
    { description: event.target.value }
  )

  onDurationLimitChange = (event: SyntheticInputEvent<HTMLInputElement>) => this.setState(
    { durationLimit: event.target.value ? Number(event.target.value) : null }
  )

  onOwnerChange = (owner: UserType) => this.setState({ owner })

  render () {
    const { tasks, title, description, durationLimit, owner, selectedTaskId } = this.state
    const { users } = this.props
    const processedNodes = calcEndDates(tasks)
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
        <Button icon='floppy-disk' text='Save' intent='primary'/>
        <H2 style={{
          display: 'inline',
          marginLeft: '40px'
        }}>Create Process Template</H2>
      </div>
      <div style={{ display: 'flex' }}>
        <Label>
          Title
          <InputGroup large style={{ minWidth: '500px' }} onChange={this.onTitleChange} value={title}
                      placeholder='Add process template title...'/>
        </Label>
      </div>
      <div style={{
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between'
      }}>
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          width: '30%'
        }}>
          <Label>Description
            <AutoSizeTextArea
              style={{
                resize: 'none'
              }}
              minRows={4}
              className='bp3-fill'
              onChange={this.onDescriptionChange}
              value={description}
              placeholder={'Add description...\n\nWhat is this process about?\nWhen should it be initiated?'}/>
          </Label>
        </div>
        <div style={{
          display: 'flex',
          flexDirection: 'column',
          width: '30%'
        }}>
          <Label>Duration limit
            <InputGroup
              type='number'
              style={{ resize: 'none' }}
              onChange={this.onDurationLimitChange}
              value={durationLimit}/>
          </Label>
          <Label>Owner
            <UserSelect users={Array.from(users.values())} activeItem={owner} onItemSelect={this.onOwnerChange}/>
          </Label>
        </div>
        <div style={{ width: '30%' }}/>
      </div>
      <div style={{ display: 'flex' }}>
        <TaskList selectedId={selectedTaskId} taskTemplates={processedNodes} createTask={this.createTask}
                  selectTaskId={this.selectTaskId}/>
        <ProcessChart tasks={processedNodes}/>
      </div>
      {this.renderTaskTemplateEditor()}
    </div>
  }
}

const promiseCreator = () => new UserApi().getUsers().then(users => ({ users }))
export default withPromiseResolver<*, *>(promiseCreator)(CreateProcessTemplate)
