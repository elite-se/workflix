// @flow

import type { Node } from 'react'
import React from 'react'
import { flatMap } from 'lodash'
import { Colors } from '@blueprintjs/core'
import type { ProcessedTaskTemplateType } from './CreateProcessTemplate'

type PropsType = {
  tasks: Array<ProcessedTaskTemplateType> /* sorted by calculated startDate */
}

type StateType = {
  width: ?number
}

export const ITEM_HEIGHT = 50
const NODE_STROKE_WIDTH = 15
const EDGE_STROKE_WIDTH = 2
const STROKE_RADIUS = 20

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
    const { tasks } = this.props
    if (tasks.length === 0) {
      return null
    }
    const lastEndDate = Math.max(...tasks.map(task => task.endDate))
    const scale = width / lastEndDate

    return <svg width={width} height={tasks.length * ITEM_HEIGHT} style={{ position: 'absolute' }}>
      <defs>
        <marker id='Triangle' viewBox='0 0 10 10' refX='10' refY='5'
                markerUnits='strokeWidth' markerWidth='5' markerHeight='5'
                orient='auto'>
          <path d='M 0 0 L 10 5 L 0 10 z' fill={Colors.GRAY1}/>
        </marker>
      </defs>
      {[
        ...flatMap(tasks, (node, index) =>
          node.predecessors
            .map(id => tasks.findIndex(x => x.id === id))
            .map(predIndex => {
              const pred = tasks[predIndex]
              return <path key={`${index},${predIndex}`}
                           markerEnd='url(#Triangle)'
                           d={`M ${(pred.startDate + pred.endDate) / 2 * scale} ${(predIndex + 1 / 2) * ITEM_HEIGHT}
                               V ${(index + 1 / 2) * ITEM_HEIGHT - STROKE_RADIUS}
                               q 0 ${STROKE_RADIUS} ${STROKE_RADIUS} ${STROKE_RADIUS}
                               H ${node.startDate * scale}`}
                           fill='none'
                           stroke={Colors.GRAY1}
                           strokeWidth={EDGE_STROKE_WIDTH}
              />
            })
        ),
        tasks.map((node, index) => (
          <path key={index}
                d={`M ${node.startDate * scale + NODE_STROKE_WIDTH / 2} ${(index + 1 / 2) * ITEM_HEIGHT}
                    h ${node.estimatedDuration * scale - NODE_STROKE_WIDTH}`}
                strokeWidth={NODE_STROKE_WIDTH}
                strokeLinecap='round'
                stroke={Colors.BLUE1}/>
        ))
      ]}
    </svg>
  }

  render () {
    const div = this.div
    return <div ref={this.setDivRef} style={{
      flex: 1,
      position: 'relative'
    }}>
      {div && this.renderSvg(div.getBoundingClientRect().width)}
    </div>
  }
}

export default ProcessChart
