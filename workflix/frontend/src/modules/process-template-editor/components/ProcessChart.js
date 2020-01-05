// @flow

import type { Node } from 'react'
import React from 'react'
import { Colors } from '@blueprintjs/core'
import type { ProcessedNodeType } from '../graph-utils'
import type { IncompleteTaskTemplateType } from '../ProcessTemplateEditorTypes'

type PropsType = {
  tasks: ProcessedNodeType<IncompleteTaskTemplateType>[] /* sorted by calculated startDate */
}

type StateType = {
  width: ?number
}

export const ITEM_HEIGHT = 50
const NODE_STROKE_WIDTH = 15
const EDGE_STROKE_WIDTH = 2
const STROKE_RADIUS = 20
const HORIZONTAL_PADDING = 10

class ProcessChart extends React.Component<PropsType, StateType> {
  resizeObserver: ResizeObserver = new ResizeObserver(entries => this.updateWidth(entries[0]?.contentRect.width))

  setDivRef = (div: HTMLDivElement | null) => {
    if (div) {
      this.resizeObserver.observe(div)
      this.updateWidth(div.getBoundingClientRect().width)
    }
  }

  updateWidth = (width: number | null) => this.setState({ width })

  state = {
    width: null
  }

  componentWillUnmount () {
    this.resizeObserver.disconnect()
  }

  renderSvg (): Node {
    const { tasks } = this.props
    const { width } = this.state
    if (tasks.length === 0 || !width) {
      return null
    }
    const lastEndDate = Math.max(...tasks.map(task => task.endDate))
    const drawWidth = width - 2 * HORIZONTAL_PADDING
    const scale = drawWidth / lastEndDate

    return <svg width={width} height={tasks.length * ITEM_HEIGHT} style={{ position: 'absolute' }}
                viewBox={`-${HORIZONTAL_PADDING} 0 ${width} ${tasks.length * ITEM_HEIGHT}`}>
      <defs>
        <marker id='TriangleGray' viewBox='0 0 10 10' refX='10' refY='5'
                markerUnits='strokeWidth' markerWidth='5' markerHeight='5'
                orient='auto'>
          <path d='M 0 0 L 10 5 L 0 10' strokeWidth={2} strokeLinecap='round' stroke={Colors.GRAY1} fill='none'/>
        </marker>
        <marker id='TriangleRed' viewBox='0 0 10 10' refX='10' refY='5'
                markerUnits='strokeWidth' markerWidth='5' markerHeight='5'
                orient='auto'>
          <path d='M 0 0 L 10 5 L 0 10' strokeWidth={2} strokeLinecap='round' stroke={Colors.RED1} fill='none'/>
        </marker>
      </defs>
      {[
        tasks.map((node, index) => (index % 2 === 0 &&
          <rect key={index} x={-HORIZONTAL_PADDING} y={index * ITEM_HEIGHT}
                height={ITEM_HEIGHT} width={width} fill={Colors.LIGHT_GRAY4}/>
        )),
        ...tasks.flatMap((node, index) =>
          node.data.predecessors
            .map(id => tasks.findIndex(x => x.id === id))
            .map(predIndex => {
              const pred = tasks[predIndex]
              const criticalEdge = node.critical && pred.endDate === node.startDate
              return {
                zIndex: criticalEdge ? 1 : 0,
                path: <path key={`${index},${predIndex}`}
                            markerEnd={`url(#${criticalEdge ? 'TriangleRed' : 'TriangleGray'})`}
                            d={`M ${(pred.startDate + pred.endDate) / 2 * scale} ${(predIndex + 1 / 2) * ITEM_HEIGHT}
                               V ${(index + 1 / 2) * ITEM_HEIGHT - STROKE_RADIUS}
                               q 0 ${STROKE_RADIUS} ${STROKE_RADIUS} ${STROKE_RADIUS}
                               H ${node.startDate * scale}`}
                            fill='none'
                            stroke={criticalEdge ? Colors.RED1 : Colors.GRAY1}
                            strokeWidth={EDGE_STROKE_WIDTH}
                />
              }
            })
        ).sort((e1, e2) => e1.zIndex - e2.zIndex)
          .map(edge => edge.path),
        tasks.map((node, index) => (
          <path key={index}
                d={`M ${node.startDate * scale + NODE_STROKE_WIDTH / 2} ${(index + 1 / 2) * ITEM_HEIGHT}
                    h ${(node.data.estimatedDuration || 0) * scale - NODE_STROKE_WIDTH}`}
                strokeWidth={NODE_STROKE_WIDTH}
                strokeLinecap='round'
                stroke={Colors.BLUE1}/>
        ))
      ]}
    </svg>
  }

  render () {
    return <div ref={this.setDivRef} style={{
      flex: 1,
      position: 'relative'
    }}>
      {this.renderSvg()}
    </div>
  }
}

export default ProcessChart
