// @flow

import React from 'react'
import ProcessChart from './ProcessChart'
import { times } from 'lodash'
import { Drawer, H2 } from '@blueprintjs/core'
import TaskList from './TaskList'
import MOCK_TASK_TEMPLATES from './mockTasks'
import TaskTemplateEditor from './TaskTemplateEditor'
import type { TaskTemplateType } from '../../../modules/datatypes/Task'

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
  selectedTaskId: ?number
}

class CreateProcessTemplate extends React.Component<{}, StateType> {
  state = {
    tasks: MOCK_TASK_TEMPLATES,
    selectedTaskId: null
  }

  editTask = (id: number) => {
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
        name: 'New Task',
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
      {task && <TaskTemplateEditor task={task} onChange={this.taskChanged} allTasks={tasks}/>}
    </Drawer>
  }

  render () {
    const processedNodes = calcEndDates(this.state.tasks)
    return <div style={{
      flex: 1,
      display: 'flex',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>Create a new Process Template</H2>
      <div style={{ display: 'flex' }}>
        <TaskList taskTemplates={processedNodes} createTask={this.createTask} editTask={this.editTask}/>
        <ProcessChart tasks={processedNodes}/>
      </div>
      {this.renderTaskTemplateEditor()}
    </div>
  }
}

export default CreateProcessTemplate
