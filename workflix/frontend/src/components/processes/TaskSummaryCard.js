// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import { Card, Colors } from '@blueprintjs/core'
import styled from 'styled-components'
import { Elevation as ELEVATION } from '@blueprintjs/core/lib/cjs/common/elevation'

const FinishedTaskStyling = styled<{ taskFinished: boolean }, {}, 'div'>('div')`
  margin: 3px;
  ${props => props.taskFinished ? `
  & > * {
    background: ${Colors.GREEN4};
  }
` : ''}
`

type PropsType = {
  task: TaskType,
  selected: boolean,
  onTaskSelected: (TaskType) => void
}

class TaskSummaryCard extends React.Component<PropsType> {
  onClick = () => this.props.onTaskSelected(this.props.task)

  render () {
    const userIdDisplayLength = 5
    const task = this.props.task
    return <FinishedTaskStyling taskFinished={task.status === 'CLOSED'}>
        <Card interactive elevation={this.props.selected ? ELEVATION.FOUR : undefined}
          onClick={this.onClick}>
          <p><b>{task.templateName}</b></p>
          <p>{task.assignments.length === 0 ? ''
          : `Assigned to: ${
            task.assignments.map(assignee => (
                assignee.assigneeId.substr(0, userIdDisplayLength)
            )).join(', ')}`}</p>
        </Card>
      </FinishedTaskStyling>
  }
}
export default TaskSummaryCard
