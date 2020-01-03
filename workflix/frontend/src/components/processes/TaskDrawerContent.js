// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import styled from 'styled-components'
import TaskComments from './TaskComments'
import type { UserType } from '../../datatypes/models'

const StyledContainer = styled<{}, {}, 'div'>('div')`
  margin: 8px;
`

type PropsType = {
  task: TaskType,
  users: Map<string, UserType>
}

class TaskDrawerContent extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    return <StyledContainer>
      <p>{task.taskTemplate.description}</p>

      <h4>Comments</h4>
      <TaskComments task={task} users={this.props.users}/>
    </StyledContainer>
  }
}
export default TaskDrawerContent
