// @flow

import React from 'react'
import type { Node } from 'react'
import { flatMap } from 'lodash'
import { Colors } from '@blueprintjs/core'
import type { ProcessedNodeType } from './CreateProcessTemplate'

type PropsType = {
  nodes: Array<ProcessedNodeType> /* sorted by calculated endDate */
}

type StateType = {
  width: ?number
}

export const ITEM_HEIGHT = 50
const NODE_STROKE_WIDTH = 15
const EDGE_STROKE_WIDTH = 2
const STROKE_RADIUS = 15

class ProcessChart extends React.Component<PropsType, StateType> {
  div: HTMLDivElement | null
  setDivRef = (div: HTMLDivElement | null) => {
    this.div = div
    this.updateWidth()
  }

  updateWidth = () => this.setState({ width: this.div ? this.div.getBoundingClientRect().width : null })

  state = {
    width: null
  }

  componentDidMount () {
    window.addEventListener('resize', this.updateWidth)
  }

  componentWillUnmount () {
    window.removeEventListener('resize', this.updateWidth)
  }

  renderSvg (width: number): Node {
    const { nodes } = this.props
    if (nodes.length === 0) {
      return null
    }
    const maxLevel = nodes[nodes.length - 1].endDate
    const scale = width / maxLevel

    return <svg width={width} height={nodes.length * ITEM_HEIGHT} style={{ position: 'absolute'}}>
      <defs>
        <marker id='Triangle' viewBox='0 0 10 10' refX='10' refY='5'
                markerUnits='strokeWidth' markerWidth='5' markerHeight='5'
                orient='auto'>
          <path d='M 0 0 L 10 5 L 0 10 z' fill={Colors.GRAY1} />
        </marker>
      </defs>
      {[
        ...flatMap(nodes, (node, index) =>
          node.predecessors
            .map(id => nodes.findIndex(x => x.id === id))
            .map(predIndex => {
              const pred = nodes[predIndex]
              return <path key={`${index},${predIndex}`}
                           markerEnd='url(#Triangle)'
                           d={`M ${(pred.endDate - pred.duration / 2) * scale} ${(predIndex + 1 / 2) * ITEM_HEIGHT}
                               V ${(index + 1 / 2) * ITEM_HEIGHT - STROKE_RADIUS}
                               q 0 ${STROKE_RADIUS} ${STROKE_RADIUS} ${STROKE_RADIUS}
                               H ${(node.endDate - node.duration) * scale}`}
                           fill='none'
                           stroke={Colors.GRAY1}
                           strokeWidth={EDGE_STROKE_WIDTH}
              />
            })
        ),
        nodes.map((node, index) => <path key={index}
              d={`M ${(node.endDate - node.duration) * scale + NODE_STROKE_WIDTH / 2} ${(index + 1 / 2) * ITEM_HEIGHT}
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
    return <div ref={this.setDivRef} style={{ flex: 1, position: 'relative' }}>
      {div && this.renderSvg(div.getBoundingClientRect().width)}
    </div>
  }
}

export default ProcessChart
