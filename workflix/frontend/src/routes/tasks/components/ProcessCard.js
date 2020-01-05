// @flow

import React from 'react'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import { Card, H3, ProgressBar, Tooltip } from '@blueprintjs/core'
import type { ProcessType } from '../../../modules/datatypes/Process'
import TaskSummaryCard from './TaskSummaryCard'
import { Elevation } from '@blueprintjs/core/lib/cjs/common/elevation'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import type { UserType } from '../../../modules/datatypes/User'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

const CardWithMargin: StyledComponent<{}, {}, *> = styled(Card)`
  margin: 5px;
  width: 250px;
  background: #FAFAFA;
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  align-items: stretch;
`

const TaskList = styled<{}, {}, 'div'>('div')`
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
  flex-grow: 1;
`
const ProcessProgress = styled(ProgressBar)`
  margin-top: 10px;
`

type PropsType = {
  process: ProcessType,
  selectedTask: ?TaskType,
  onTaskSelected: TaskType => void,
  users: Map<string, UserType>,
  taskTemplates: Map<number, TaskTemplateType>
}

const statusTranslation = {
  ABORTED: 'aborted',
  CLOSED: 'closed',
  RUNNING: 'running'
}

const HUNDRED = 100

class ProcessCard extends React.Component<PropsType> {
  isSelected (task: TaskType): boolean {
    const selectedTask = this.props.selectedTask
    return selectedTask != null && task.id === selectedTask.id
  }

  render () {
    const process = this.props.process
    const [progressIntent, progressValue] =
      process.status === 'ABORTED'
        ? [Intent.DANGER, 1]
        : process.status === 'CLOSED'
          ? [Intent.SUCCESS, 1]
          : [Intent.PRIMARY, process.progress / HUNDRED]
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
                             users={this.props.users}
                             taskTemplates={this.props.taskTemplates}/>
          ))
        }
      </TaskList>
      <Tooltip content={`This process is ${statusTranslation[process.status]}.`} fill>
        <ProcessProgress animate={false} intent={progressIntent} value={progressValue}/>
      </Tooltip>
    </CardWithMargin>
  }
}

export default ProcessCard
