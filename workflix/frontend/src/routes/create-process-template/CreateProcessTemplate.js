// @flow

import React from 'react'
import ProcessChart from './ProcessChart'
import { times } from 'lodash'
import { H2 } from '@blueprintjs/core'
import TaskList from './TaskList'
import MOCK_TASKS from './mockTasks'
import TaskTemplateEditor from './TaskTemplateEditor'

export type NodeType = {|
  id: number,
  predecessors: Array<number>,
  title: string,
  duration: number
|}

export type ProcessedNodeType = {|
  ...NodeType,
  endDate: number
|}

const calcEndDates = (nodes: Array<NodeType>): Array<ProcessedNodeType> => {
  const leveledNodes = nodes.map(node => ({
    ...node,
    endDate: 0
  }))

  times(leveledNodes.length - 1, () => { // Bellman-Ford
    for (const node of leveledNodes) {
      node.endDate = Math.max(
        0,
        ...(node.predecessors.map(id => leveledNodes.find(x => x.id === id)?.endDate || 0))
      ) + node.duration
    }
  })

  return leveledNodes.sort((node1, node2) => node1.endDate - node1.duration - node2.endDate + node2.duration)
}

type StateType = {
  nodes: Array<NodeType>,
  selectedNode: ?number
}

class CreateProcessTemplate extends React.Component<{}, StateType> {
  state = {
    nodes: MOCK_TASKS,
    selectedNode: null
  }

  editTask = (id: number) => {
    this.setState({ selectedNode: id })
  }

  selectedTaskChanged = (task: NodeType) => {
    this.setState(state => ({
      nodes: state.nodes.map(node => node.id === state.selectedNode ? task : node)
    }))
  }

  createTask = () => {
    this.setState(state => {
      const newId = Math.max(...state.nodes.map(node => node.id), -1) + 1
      const newTask: NodeType = {
        id: newId,
        predecessors: [],
        title: 'New Task',
        duration: 1
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
        <TaskList nodes={processedNodes} createTask={this.createTask} editTask={this.editTask} />
        <ProcessChart nodes={processedNodes} />
      </div>
      {this.renderTaskTemplateEditor()}
    </div>
  }
}

export default CreateProcessTemplate
