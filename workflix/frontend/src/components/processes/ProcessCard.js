// @flow

import React from 'react'
import styled from 'styled-components'
import { Card, H3 } from '@blueprintjs/core'
import type ProcessType from '../../datatypes/ProcessType'
import TaskSummaryCard from './TaskSummaryCard'

const CardWrapper = styled<{}, {}, 'div'>('div')`
  & > * {
    margin: 5px;
  }
`
const TaskList = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: column;
`

class ProcessCard extends React.Component<{ process: ProcessType }, {}> {
  render () {
    const process = this.props.process
    return <CardWrapper><Card interactive>
      <H3>{process.masterData.title}</H3>
      <TaskList>
        {
          process.tasks.map(task => (
            <TaskSummaryCard key={task.id} task={task} />
          ))
        }
      </TaskList>
    </Card></CardWrapper>
  }
}

export default ProcessCard
