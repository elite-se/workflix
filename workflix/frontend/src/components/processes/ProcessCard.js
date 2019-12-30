// @flow

import React from 'react'
import styled from 'styled-components'
import { Card, H3, ProgressBar } from '@blueprintjs/core'
import type { ProcessType } from '../../datatypes/ProcessType'
import TaskSummaryCard from './TaskSummaryCard'
import type { StyledComponent } from 'styled-components'
import { Elevation } from '@blueprintjs/core/lib/cjs/common/elevation'
import type { TaskType } from '../../datatypes/TaskType'
import type { UserType } from '../../datatypes/models'

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

type PropsType = {
  process: ProcessType,
  selectedTask: ?TaskType,
  onTaskSelected: TaskType => void,
  users: Map<string, UserType>
}

class ProcessCard extends React.Component<PropsType> {
  isSelected (task: TaskType): boolean {
    const selectedTask = this.props.selectedTask
    return selectedTask != null && task.id === selectedTask.id
  }

  render () {
    const process = this.props.process
    const taskProgress = process.progress
    const isSelected = !!process.tasks.find(task => this.isSelected(task))
    return <CardWithMargin interactive elevation={isSelected ? Elevation.FOUR : undefined}>
      <H3>{process.title} (#{process.id})</H3>
      <TaskList>
        {
          process.tasks.map(task => (
            <TaskSummaryCard key={task.id}
              task={task}
              selected={this.isSelected(task)}
              onTaskSelected={this.props.onTaskSelected}
              users={this.props.users} />
          ))
        }
      </TaskList>
      <ProcessProgress animate={false} intent='success' value={taskProgress} />
    </CardWithMargin>
  }
}

export default ProcessCard
