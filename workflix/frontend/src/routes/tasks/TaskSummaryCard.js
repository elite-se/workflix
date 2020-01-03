// @flow

import React from 'react'
import type { TaskTemplateType, TaskType } from '../../modules/datatypes/Task'
import { Card, Colors } from '@blueprintjs/core'
import styled from 'styled-components'
import { Elevation as ELEVATION } from '@blueprintjs/core/lib/cjs/common/elevation'
import type { UserType } from '../../modules/datatypes/User'

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
  onTaskSelected: (TaskType) => void,
  users: Map<string, UserType>,
  taskTemplates: Map<number, TaskTemplateType>
}

class TaskSummaryCard extends React.Component<PropsType> {
  onClick = () => this.props.onTaskSelected(this.props.task)

  render () {
    const task = this.props.task
    const taskTemplate = this.props.taskTemplates.get(task.taskTemplateId)
    return <FinishedTaskStyling taskFinished={task.status === 'CLOSED'}>
      <Card interactive elevation={this.props.selected ? ELEVATION.FOUR : undefined}
            onClick={this.onClick}>
        <p><b>{taskTemplate ? taskTemplate.name : ''}</b></p>
        <p>{task.assignments.length === 0 ? 'No Assignees'
          : <span>Assigned to: {
            task.assignments.map(assignee => {
              const user = this.props.users.get(assignee.assigneeId)
              return <span key={assignee.assigneeId} className='comma'>
                {user ? user.name : assignee.assigneeId}
              </span>
            })
          }</span>}</p>
      </Card>
    </FinishedTaskStyling>
  }
}

export default TaskSummaryCard
