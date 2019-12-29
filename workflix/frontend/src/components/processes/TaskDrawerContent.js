// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import styled from 'styled-components'
import TaskAssignmentSelect from './TaskAssignmentSelect'
import TaskComments from './TaskComments'
import type { UserType } from '../../datatypes/models'

const StyledContainer = styled<{}, {}, 'div'>('div')`
  margin: 8px;
`

type PropsType = {
  task: TaskType,
  onTaskModified: (TaskType) => void,
  users: Map<string, UserType>
}

class TaskDrawerContent extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    return <StyledContainer>
      <h4>Description</h4>
      <p>{task.templateDescription}</p>

      <h4>Assignee</h4>
      <TaskAssignmentSelect task={task} onTaskModified={this.props.onTaskModified} users={this.props.users} />

      <h4>Comments</h4>
      <TaskComments task={task} users={this.props.users} />
    </StyledContainer>
  }
}

export default TaskDrawerContent
