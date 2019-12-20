// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import styled from 'styled-components'
import TaskAssignmentSelect from './TaskAssignmentSelect'

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
      <h4>Description</h4>
      <p>{task.templateDescription}</p>

      <h4>Assignee</h4>
      <TaskAssignmentSelect task={task} />
    </StyledContainer>
  }
}

export default TaskDrawerContent
