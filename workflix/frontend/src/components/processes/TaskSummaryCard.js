// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import { Card, Colors } from '@blueprintjs/core'
import styled from 'styled-components'
import { Elevation as ELEVATION } from '@blueprintjs/core/lib/cjs/common/elevation'
import LoadingUsername from '../LoadingUsername'

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
    const task = this.props.task
    return <FinishedTaskStyling taskFinished={task.status === 'CLOSED'}>
      <Card interactive elevation={this.props.selected ? ELEVATION.FOUR : undefined}
            onClick={this.onClick}>
        <p><b>{task.templateName}</b></p>
        <p>{task.assignments.length === 0 ? 'No Assignees'
          : <span>Assigned to: {
            task.assignments.map((assignee, index) =>
              <span key={assignee.assigneeId}>{index > 0 ? ', ' : ''}
                <LoadingUsername userId={assignee.assigneeId} />
              </span>
            )
          }</span>}</p>
      </Card>
    </FinishedTaskStyling>
  }
}

export default TaskSummaryCard
