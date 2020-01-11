// @flow

import React from 'react'
import type { TaskStatusType, TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import { Card, Colors, Icon, Tooltip } from '@blueprintjs/core'
import styled from 'styled-components'
import { Elevation as ELEVATION } from '@blueprintjs/core/lib/cjs/common/elevation'
import type { UserType } from '../../../modules/datatypes/User'
import { getCurrentUserId } from '../../../modules/common/tokenStorage'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

const SCROLL_DELAY_MS = 300

const FinishedTaskStyling = styled<{ status: TaskStatusType }, {}, 'div'>('div')`
  margin: 3px 0;
  & > * {
    ${props => props.status !== 'BLOCKED' && `
    border: 1px solid ${props.status === 'CLOSED' ? Colors.GREEN4 : Colors.ORANGE4};
    `}
    padding: 15px;
  }
`

type PropsType = {
  task: TaskType,
  selected: boolean,
  onTaskSelected: (TaskType) => void,
  users: Map<string, UserType>,
  processGroupId: number,
  taskTemplates: Map<number, TaskTemplateType>
}

class TaskSummaryCard extends React.Component<PropsType> {
  containerDiv: ?HTMLDivElement = null

  onClick = (event: SyntheticMouseEvent<HTMLElement>) => {
    event.preventDefault()
    this.props.onTaskSelected(this.props.task)
  }

  ref = (elem: ?HTMLDivElement) => {
    this.containerDiv = elem
  }

  isSuggestedTask (taskTemplate: TaskTemplateType): boolean {
    const activeUser = this.props.users.get(getCurrentUserId())
    if (!activeUser) {
      return false
    }
    return activeUser.userRoleIds.includes(taskTemplate.responsibleUserRoleId) &&
      activeUser.processGroupIds.includes(this.props.processGroupId)
  }

  render () {
    const { task, taskTemplates, users } = this.props
    const template = taskTemplates.get(task.taskTemplateId)
    if (!template) {
      return <div>Something went wrong.</div>
    }
    const shouldAssign = task.assignments.length < template.necessaryClosings && this.isSuggestedTask(template)
    const intent = task.status === 'CLOSED' ? Intent.SUCCESS : task.status === 'RUNNING' ? Intent.WARNING : Intent.NONE
    const tooltip = task.status === 'CLOSED' ? 'This task is already done.'
      : task.status === 'BLOCKED' ? 'This task depends on preceding tasks that are not yet done.'
        : task.status === 'RUNNING' ? `This task is waiting for being done.${shouldAssign ? ' Assign now!' : ''}`
          : null
    return <div ref={this.ref}>
      <Tooltip intent={intent} content={tooltip} disabled={!tooltip} targetTagName='div'>
        <FinishedTaskStyling status={task.status}>
          <Card interactive elevation={this.props.selected ? ELEVATION.FOUR : undefined}
                onClick={this.onClick}>
            <span><b>{template.name}</b></span><br/>
            <small>{task.assignments.length === 0 ? 'No assignees'
              : <span>{
                task.assignments.map(assignee => {
                  const user = users.get(assignee.assigneeId)
                  return user && <span key={assignee.assigneeId} className='comma'>
                {user.name}{assignee.closed && <Icon icon='small-tick'/>}
              </span>
                })
              }</span>}<br/>
              {shouldAssign && <b>Assign now!</b>}
            </small>
          </Card>
        </FinishedTaskStyling>
      </Tooltip>
    </div>
  }

  componentDidUpdate (prevProps: *) {
    setTimeout(() => {
      if (this.props.selected !== prevProps.selected) {
        this.containerDiv &&
        this.containerDiv.scrollIntoView({
          behavior: 'smooth',
          block: 'nearest',
          inline: 'center'
        })
      }
    }, SCROLL_DELAY_MS)
  }
}

export default TaskSummaryCard
