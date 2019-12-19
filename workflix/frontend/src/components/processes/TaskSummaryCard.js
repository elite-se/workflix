// @flow

import React from 'react'
import type { TaskType } from '../../datatypes/TaskType'
import { Card, Colors } from '@blueprintjs/core'
import { Link } from '@reach/router'
import styled from 'styled-components'
import type { StyledComponent } from 'styled-components'

const CustomLink: StyledComponent<{}, {}, *> = styled(Link)`
  margin: 3px;
  color: black;
  
  :hover {
    color: black;
    text-decoration: none;
  }
`

const FinishedTaskStyling = styled<{ taskFinished: boolean }, {}, 'div'>('div')`
  ${props => props.taskFinished ? `
  & > * {
    background: ${Colors.GREEN4};
  }
` : ''}
`

class TaskSummaryCard extends React.Component<{ task: TaskType}, {}> {
  render () {
    const userIdDisplayLength = 5
    const task = this.props.task
    return <CustomLink to={`/task/${task.taskId}`}>
      <FinishedTaskStyling taskFinished={task.done}>
        <Card interactive>
          <p><b>{task.templateName}</b></p>
          <p>{task.personsResponsible.length === 0 ? ''
          : `Responsible: ${
            task.personsResponsible.map(responsible => (
                responsible.personResponsibleId.substr(0, userIdDisplayLength)
            )).join(', ')}`}</p>
        </Card>
      </FinishedTaskStyling>
    </CustomLink>
  }
}
export default TaskSummaryCard
