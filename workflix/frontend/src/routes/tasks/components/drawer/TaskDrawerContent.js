// @flow

import React from 'react'
import styled from 'styled-components'
import { H4 } from '@blueprintjs/core'
import type { TaskTemplateType, TaskType } from '../../../../modules/datatypes/Task'
import type { UserType } from '../../../../modules/datatypes/User'
import TaskComments from './comments/TaskComments'
import Assignments from './assignments/Assignments'

const StyledContainer = styled<{}, {}, 'div'>('div')`
  margin: 8px;
  display: flex;
  flex-direction: column;
`
const H4WithMargin = styled(H4)`
  margin-top: 10px;
`

type PropsType = {
  task: TaskType,
  taskTemplates: Map<number, TaskTemplateType>,
  onTaskModified: (TaskType) => void,
  users: Map<string, UserType>
}

class TaskDrawerContent extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    const taskTemplate = this.props.taskTemplates.get(task.taskTemplateId)
    return <StyledContainer>
      <H4WithMargin>Description</H4WithMargin>
      <p>{taskTemplate ? taskTemplate.description : ''}</p>

      <H4WithMargin>Assignee</H4WithMargin>
      <Assignments task={task} onTaskModified={this.props.onTaskModified} users={this.props.users}/>

      <H4WithMargin>Comments</H4WithMargin>
      <TaskComments task={task} users={this.props.users} onTaskModified={this.props.onTaskModified}/>
    </StyledContainer>
  }
}

export default TaskDrawerContent
