// @flow

import React from 'react'
import ProcessChart from './ProcessChart'
import { times } from 'lodash'
import { H2 } from '@blueprintjs/core'
import TaskList from './TaskList'
import MOCK_TASK_TEMPLATES from './mockTasks'
import TaskTemplateEditor from './TaskTemplateEditor'
import type { TaskTemplateType } from '../../datatypes/TaskType'

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
  nodes: Array<TaskTemplateType>,
  selectedNode: ?number
}

class CreateProcessTemplate extends React.Component<{}, StateType> {
  state = {
    nodes: MOCK_TASK_TEMPLATES,
    selectedNode: null
  }

  editTask = (id: number) => {
    this.setState({ selectedNode: id })
  }

  selectedTaskChanged = (task: TaskTemplateType) => {
    this.setState(state => ({
      nodes: state.nodes.map(node => node.id === state.selectedNode ? task : node)
    }))
  }

  createTask = () => {
    this.setState(state => {
      const newId = Math.max(...state.nodes.map(node => node.id), -1) + 1
      const newTask: TaskTemplateType = {
        id: newId,
        predecessors: [],
        name: 'New Task',
        estimatedDuration: 1,
        description: '',
        necessaryClosings: 0
      }
      return {
        nodes: [...state.nodes, newTask],
        selectedNode: newId
      }
    })
  }

  renderTaskTemplateEditor (): React$Node {
    const task = this.state.nodes.find(task => task.id === this.state.selectedNode)
    if (!task) {
      return null
    }
    return <TaskTemplateEditor task={task} onChange={this.selectedTaskChanged} />
  }

  render () {
    const processedNodes = calcEndDates(this.state.nodes)
    return <div style={{
      flex: 1,
      display: 'flex',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>Create a new Process Template</H2>
      <div style={{ display: 'flex' }}>
        <TaskList taskTemplates={processedNodes} createTask={this.createTask} editTask={this.editTask} />
        <ProcessChart tasks={processedNodes} />
      </div>
      {this.renderTaskTemplateEditor()}
    </div>
  }
}

export default CreateProcessTemplate
