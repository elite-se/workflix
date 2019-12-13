// @flow

import React from 'react'
import type TaskType from '../../datatypes/TaskType'
import { Card, Colors } from '@blueprintjs/core'
import { Link } from '@reach/router'
import styled from 'styled-components'

const CardWrapper = styled<{}, {}, 'div'>('div')`
  margin: 3px;
`

const CustomLink = styled(Link)`
  color: black;
  
  :hover {
    color: black;
    text-decoration: none;
  }
  
`

const FinishedTaskStyling = styled<{ taskFinished: boolean }, {}, 'div'>('div')(props => props.taskFinished ? `
  & > * {
    background: ${Colors.GREEN4};
  }
` : '')

class TaskSummaryCard extends React.Component<{ task: TaskType}, {}> {
  render () {
    const task = this.props.task
    return <CardWrapper><CustomLink to={`/task/${task.id}`}>
      <FinishedTaskStyling taskFinished={task.finished}><Card interactive>{task.name}</Card></FinishedTaskStyling>
    </CustomLink></CardWrapper>
  }
}
export default TaskSummaryCard
