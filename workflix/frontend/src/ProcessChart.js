// @flow

import React from 'react'
import type { Node } from 'react'
import { flatMap, times } from 'lodash'
import { Colors } from '@blueprintjs/core'

/* eslint-disable no-magic-numbers */

type LeveledNodeType = {
  id: number,
  endDate: number,
  predecessors: Array<number>,
  duration: number
}

type NodeType = {
  id: number,
  predecessors: Array<number>,
  duration: number
}

type StateType = {
  tasks: Array<NodeType>,
  divRefSet: boolean
}

const calcEndDates = (nodes: Array<NodeType>): Array<LeveledNodeType> => {
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

/*
levels: 1   2   3   4   5
graph:  1 ----- 3 - 6
              /       \
        0 - 2 - 4 - 5 - 7
 */

const ITEM_HEIGHT = 50
const NODE_STROKE_WIDTH = 15
const EDGE_STROKE_WIDTH = 2
const STROKE_RADIUS = 15

class ProcessChart extends React.Component<{}, StateType> {
  div: HTMLDivElement | null
  setDivRef = (div: HTMLDivElement | null) => {
    this.div = div
    this.setState({ divRefSet: !!div })
  }

  state = {
    tasks: [
      {
        id: 0,
        predecessors: [],
        duration: 1
      },
      {
        id: 1,
        predecessors: [],
        duration: 2
      },
      {
        id: 2,
        predecessors: [0],
        duration: 1.4
      },
      {
        id: 3,
        predecessors: [1, 2],
        duration: 1.3
      },
      {
        id: 4,
        predecessors: [2],
        duration: 2
      },
      {
        id: 5,
        predecessors: [4],
        duration: 2
      },
      {
        id: 6,
        predecessors: [3],
        duration: 1
      },
      {
        id: 7,
        predecessors: [5, 6],
        duration: 1
      }
    ],
    divRefSet: false
  }

  renderSvg (width: number): Node {
    const leveledNodes = calcEndDates(this.state.tasks)
    const maxLevel = leveledNodes[leveledNodes.length - 1].endDate
    const scale = width / maxLevel

    return <svg width={width} height={leveledNodes.length * ITEM_HEIGHT}>
      <defs>
        <marker id='Triangle' viewBox='0 0 10 10' refX='10' refY='5'
                markerUnits='strokeWidth' markerWidth='5' markerHeight='5'
                orient='auto'>
          <path d='M 0 0 L 10 5 L 0 10 z' fill={Colors.GRAY1} />
        </marker>
      </defs>
      {[
        ...flatMap(leveledNodes, (node, index) =>
          node.predecessors
            .map(id => leveledNodes.findIndex(x => x.id === id))
            .map(predIndex => {
              const pred = leveledNodes[predIndex]
              return <path key={`${index},${predIndex}`}
                           markerEnd='url(#Triangle)'
                           d={`M ${(pred.endDate - pred.duration / 2) * scale} ${(predIndex + 0.5) * ITEM_HEIGHT}
                               V ${(index + 0.5) * ITEM_HEIGHT - STROKE_RADIUS}
                               q 0 ${STROKE_RADIUS} ${STROKE_RADIUS} ${STROKE_RADIUS}
                               H ${(node.endDate - node.duration) * scale}`}
                           fill='none'
                           stroke={Colors.GRAY1}
                           strokeWidth={EDGE_STROKE_WIDTH}
              />
            })
        ),
        leveledNodes.map((node, index) => <path key={index}
              d={`M ${(node.endDate - node.duration) * scale + NODE_STROKE_WIDTH / 2} ${(index + 0.5) * ITEM_HEIGHT}
                  h ${node.duration * scale - NODE_STROKE_WIDTH}`}
              strokeWidth={NODE_STROKE_WIDTH}
              strokeLinecap='round'
              stroke={Colors.BLUE1} />
        )
      ]}
    </svg>
  }

  render () {
    const div = this.div
    return <div ref={this.setDivRef} style={{ flex: 1 }}>
      {div && this.renderSvg(div.getBoundingClientRect().width)}
    </div>
  }
}

export default ProcessChart
