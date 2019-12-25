// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import styled from 'styled-components'
import TaskComments from './TaskComments'

const StyledContainer = styled<{}, {}, 'div'>('div')`
  margin: 8px;
`

type PropsType = {
  task: TaskType
}

class TaskDrawerContent extends React.Component<PropsType> {
  render () {
    const task = this.props.task
    return <StyledContainer>
      <p>{task.templateDescription}</p>

      <h4>Comments</h4>
      <TaskComments task={task} />
    </StyledContainer>
  }
}
export default TaskDrawerContent
