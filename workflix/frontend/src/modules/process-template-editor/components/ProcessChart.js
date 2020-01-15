// @flow

import type { Node } from 'react'
import React from 'react'
import { Colors } from '@blueprintjs/core'
import type { ProcessedNodeType } from '../graph-utils'
import { renderFloat } from '../../common/constants'

export type ChartNodeType = {
  id: number,
  predecessors: number[],
  color: string,
  critical: boolean,
  startDate: number,
  estimatedDuration: number,
  endDate: number
}

export const chartNodeFromProcessedNode = (node: ProcessedNodeType<*, *>, color: string): ChartNodeType => ({
  id: node.id,
  predecessors: node.data.predecessors,
  color,
  critical: node.critical,
  startDate: node.startDate,
  estimatedDuration: node.estimatedDuration,
  endDate: node.endDate
})

type PropsType = {
  tasks: ChartNodeType[], /* sorted by calculated startDate */
  selectTaskId?: (id: number) => void,
  mini?: boolean
}

type StateType = {
  width: ?number
}

export const ITEM_HEIGHT = 50
const NODE_HEIGHT = 15
const EDGE_STROKE_WIDTH = 2
const STROKE_RADIUS = 20
const HORIZONTAL_PADDING = 10
const LEGEND_TEXT_OFFSET = 10

class ProcessChart extends React.Component<PropsType, StateType> {
  resizeObserver: ResizeObserver = new ResizeObserver(entries => this.updateWidth(entries[0]?.contentRect.width))

  setDivRef = (div: HTMLDivElement | null) => {
    if (div) {
      this.resizeObserver.observe(div)
      this.updateWidth(div.getBoundingClientRect().width)
    }
  }

  updateWidth = (width: number | null) => this.setState({ width })
  onClick = (id: number) => () => this.props.selectTaskId && this.props.selectTaskId(id)

  state = {
    width: null
  }

  componentWillUnmount () {
    this.resizeObserver.disconnect()
  }

  renderSvg (): Node {
    const { tasks, mini } = this.props
    const { width } = this.state
    if (tasks.length === 0 || !width) {
      return null
    }
    const lastEndDate = Math.max(...tasks.map(task => task.endDate))
    const drawWidth = width - 2 * HORIZONTAL_PADDING
    const scale = drawWidth / lastEndDate
    const miniFactor = mini ? 1 / 2 : 1
    const itemHeight = ITEM_HEIGHT * miniFactor
    const strokeRadius = STROKE_RADIUS * miniFactor

    return <svg width={width} height={(tasks.length + (mini ? 0 : 1)) * itemHeight} style={{ position: 'absolute' }}
                viewBox={`-${HORIZONTAL_PADDING} 0 ${width} ${(tasks.length + (mini ? 0 : 1)) * itemHeight}`}>
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

        !mini && tasks.map((node, index) => (index % 2 === 0 &&
          <rect key={index} x={-HORIZONTAL_PADDING} y={index * itemHeight} className='even-proc-chart-row'
                height={ITEM_HEIGHT * miniFactor} width={width}/>
        )),
        !mini && <text x={drawWidth} y={(tasks.length + 1 / 2) * itemHeight + LEGEND_TEXT_OFFSET}
                textAnchor='end'>Critical Length: {renderFloat(lastEndDate)}</text>,
        ...tasks.flatMap((node, index) =>
          node.predecessors
            .map(id => tasks.findIndex(x => x.id === id))
            .map(predIndex => {
              const pred = tasks[predIndex]
              const criticalEdge = node.critical && pred.endDate === node.startDate
              return {
                zIndex: criticalEdge ? 1 : 0,
                path: <path key={`${index},${predIndex}`}
                            markerEnd={`url(#${criticalEdge ? 'TriangleRed' : 'TriangleGray'})`}
                            d={`M ${(pred.startDate + pred.endDate) / 2 * scale} ${(predIndex + 1 / 2) * itemHeight}
                               V ${(index + 1 / 2) * itemHeight - strokeRadius}
                               q 0 ${strokeRadius} ${strokeRadius} ${strokeRadius}
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
          <rect key={node.id} onClick={this.onClick(node.id)}
                style={{
                  transform: `scaleX(${scale})`,
                  transformOrigin: '0 0',
                  cursor: 'pointer',
                  transition: 'x 0.2s, y 0.5s, width 0.2s, fill 0.3s'
                }}
                x={node.startDate} y={index * itemHeight + (itemHeight - NODE_HEIGHT) / 2}
                width={node.estimatedDuration} height={NODE_HEIGHT}
                rx={NODE_HEIGHT / 2 / scale} ry={NODE_HEIGHT / 2}
                fill={node.color}/>
        ))
      ]}
    </svg>
  }

  render () {
    const { tasks, mini } = this.props
    const itemHeight = ITEM_HEIGHT * (mini ? 1 / 2 : 1)
    return <div ref={this.setDivRef} style={{
      flex: 1,
      position: 'relative',
      height: (tasks.length + (mini ? 0 : 1)) * itemHeight
    }}>
      {this.renderSvg()}
    </div>
  }
}

export default ProcessChart
