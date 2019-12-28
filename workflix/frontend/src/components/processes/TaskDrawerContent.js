// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import styled from 'styled-components'

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
    </StyledContainer>
  }
}
export default TaskDrawerContent
