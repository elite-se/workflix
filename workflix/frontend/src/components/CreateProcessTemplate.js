// @flow

import React from 'react'
import ProcessChart, { ITEM_HEIGHT } from './ProcessChart'
import { times } from 'lodash'
import { H2 } from '@blueprintjs/core'

/* eslint-disable no-magic-numbers */

/*
graph:  1 ----- 3 - 6
              /       \
        0 - 2 - 4 - 5 - 7
 */

type NodeType = {
  id: number,
  predecessors: Array<number>,
  title: string,
  duration: number
}

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

export type ProcessedNodeType = {
  ...NodeType,
  endDate: number
}

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

  return leveledNodes.sort((node1, node2) => node1.endDate - node2.endDate)
}

class NodeList extends React.Component<{nodes: Array<ProcessedNodeType> }> {
  render () {
    return <div style={{ margin: '0px 20px', minWidth: '100px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      {
        this.props.nodes.map(
          node => <div key={node.id} style={{ height: `${ITEM_HEIGHT}px`, display: 'flex', alignItems: 'center' }}>{node.title}</div>
        )
      }
    </div>
  }
}

class CreateProcessTemplate extends React.Component<{}, { nodes: Array<NodeType> }> {
  state = { nodes: MOCK_TASKS }

  render () {
    const processedNodes = calcEndDates(this.state.nodes)
    return <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
      <H2 style={{ textAlign: 'center' }}>Create a new Process Template</H2>
      <div style={{ display: 'flex' }}>
        <NodeList nodes={processedNodes} />
        <ProcessChart nodes={processedNodes} />
      </div>
    </div>
  }
}

export default CreateProcessTemplate
