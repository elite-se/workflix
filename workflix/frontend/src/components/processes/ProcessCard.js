// @flow

import React from 'react'
import styled from 'styled-components'
import { Card, H3, ProgressBar } from '@blueprintjs/core'
import type { ProcessType } from '../../datatypes/ProcessType'
import TaskSummaryCard from './TaskSummaryCard'
import type { StyledComponent } from 'styled-components'

const CardWithMargin: StyledComponent<{}, {}, *> = styled(Card)`
  margin: 5px;
`

const TaskList = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: column;
`
const ProcessProgress = styled(ProgressBar)`
  margin-top: 7px;
`

class ProcessCard extends React.Component<{ process: ProcessType }, {}> {
  render () {
    const process = this.props.process
    const taskProgress = process.progress
    return <CardWithMargin interactive>
      <H3>{process.title} (#{process.id})</H3>
      <TaskList>
        {
          process.tasks.map(task => (
            <TaskSummaryCard key={task.taskId} task={task} />
          ))
        }
      </TaskList>
      <ProcessProgress animate={false} intent='success' value={taskProgress} />
    </CardWithMargin>
  }
}

export default ProcessCard
