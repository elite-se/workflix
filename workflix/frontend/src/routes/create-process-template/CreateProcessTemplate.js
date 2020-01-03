// @flow

import React from 'react'
import ProcessChart, { ITEM_HEIGHT } from './ProcessChart'
import { times } from 'lodash'
import { Button, H2, H3, Colors, H4 } from '@blueprintjs/core'

/* eslint-disable no-magic-numbers */

/*
graph:  1 ----- 3 - 6
              /       \
        0 - 2 - 4 - 5 - 7
 */

type NodeType = {|
  id: number,
  predecessors: Array<number>,
  title: string,
  duration: number
|}

const MOCK_TASKS: Array<NodeType> = [
  {
    id: 0,
    predecessors: [],
    duration: 1,
    title: 'Do stuff 0'
  },
  {
    id: 1,
    predecessors: [],
    duration: 2,
    title: 'Do stuff 1'
  },
  {
    id: 2,
    predecessors: [0],
    duration: 1.4,
    title: 'Do stuff 2'
  },
  {
    id: 3,
    predecessors: [1, 2],
    duration: 1.3,
    title: 'Do stuff 3'
  },
  {
    id: 4,
    predecessors: [2],
    duration: 2,
    title: 'Do stuff 4'
  },
  {
    id: 5,
    predecessors: [4],
    duration: 2,
    title: 'Do stuff 5'
  },
  {
    id: 6,
    predecessors: [3],
    duration: 1,
    title: 'Do stuff 6'
  },
  {
    id: 7,
    predecessors: [5, 6],
    duration: 1,
    title: 'Do stuff 7'
  }
]

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
      node.endDate = Math.max(0, ...(node.predecessors.map(id => leveledNodes.find(x => x.id === id)?.endDate || 0))) + node.duration
    }
  })

  return leveledNodes.sort((node1, node2) => node1.endDate - node1.duration - node2.endDate + node2.duration)
}

class NodeList extends React.Component<{nodes: Array<ProcessedNodeType>, createTask: () => void, editTask: (id: number) => void }> {
  editTask = (id: number) => () => {
    this.props.editTask(id)
  }

  render () {
    return <div style={{ margin: '0px 20px', minWidth: '100px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      {
        this.props.nodes.map(
          node => <Button className='bp3-minimal'
                          onClick={this.editTask(node.id)}
                          key={node.id}
                          style={{ height: `${ITEM_HEIGHT}px`, display: 'flex', alignItems: 'center' }}>{node.title}</Button>
        )
      }
      <Button style={{ marginTop: '10px' }} className='bp3-minimal' icon='add' text='Add task' onClick={this.props.createTask} />
    </div>
  }
}

class TaskTemplateEditor extends React.Component<{ task: NodeType, onChange: (task: NodeType) => void }> {
  onTitleChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      title: event.target.value
    })
  }

  onDurationChange = (event: SyntheticInputEvent<HTMLInputElement>) => {
    this.props.onChange({
      ...this.props.task,
      duration: Number(event.target.value)
    })
  }

  render () {
    return <div style={{ paddingTop: '10px', marginTop: '10px', borderTop: `1px ${Colors.GRAY1} solid`, display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
      <H3>Edit Task Template</H3>
      <div><H4>Title:</H4> <input type='text' className='bp3-input' placeholder='Title...' value={this.props.task.title}
                                  onChange={this.onTitleChange} /></div>
      <div><H4>Duration:</H4> <input type='number' className='bp3-input' placeholder='Duration...' value={this.props.task.duration}
                                  onChange={this.onDurationChange} min={0.1} step={0.1} /></div>
    </div>
  }
}

class CreateProcessTemplate extends React.Component<{}, { nodes: Array<NodeType>, selectedNode: ?number }> {
  state = { nodes: MOCK_TASKS, selectedNode: null }

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

  renderTaskTemplateEditor () {
    const task = this.state.nodes.find(task => task.id === this.state.selectedNode)
    if (!task) { return null }
    return <TaskTemplateEditor task={task} onChange={this.selectedTaskChanged} />
  }

  render () {
    const processedNodes = calcEndDates(this.state.nodes)
    return <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      <H2 style={{ textAlign: 'center' }}>Create a new Process Template</H2>
      <div style={{ display: 'flex' }}>
        <NodeList nodes={processedNodes} createTask={this.createTask} editTask={this.editTask} />
        <ProcessChart nodes={processedNodes} />
      </div>
      {this.renderTaskTemplateEditor()}
    </div>
  }
}

export default CreateProcessTemplate
