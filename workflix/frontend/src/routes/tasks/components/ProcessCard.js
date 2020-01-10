// @flow

import React from 'react'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import { Card, H3 } from '@blueprintjs/core'
import type { ProcessType } from '../../../modules/datatypes/Process'
import TaskSummaryCard from './TaskSummaryCard'
import { Elevation } from '@blueprintjs/core/lib/cjs/common/elevation'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import type { UserType } from '../../../modules/datatypes/User'
import ProcessProgress from '../../../modules/common/components/ProcessProgress'
import { Link } from '@reach/router'

const CustomCard: StyledComponent<{}, {}, *> = styled(Card)`
  width: 250px;
  background: #FAFAFA;
  padding: 15px;
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

const CustomLink: StyledComponent<{}, {}, *> = styled(Link)`
  color: black;
  margin: 5px;
  &:hover {
    text-decoration: none;
    color: black;
  }
  > * {
    padding-bottom: 0;
  }
`

type PropsType = {
  process: ProcessType,
  selectedTask: ?TaskType,
  onTaskSelected: TaskType => void,
  users: Map<string, UserType>,
  taskTemplates: Map<number, TaskTemplateType>
}

class ProcessCard extends React.Component<PropsType> {
  isSelected (task: TaskType): boolean {
    const selectedTask = this.props.selectedTask
    return selectedTask != null && task.id === selectedTask.id
  }

  render () {
    const process = this.props.process
    const isSelected = !!process.tasks.find(task => this.isSelected(task))
    return <CustomLink to={`/processes/${process.id}`}>
      <CustomCard interactive elevation={isSelected ? Elevation.FOUR : undefined}>
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
      <ProcessProgress process={process}/>
    </CustomCard>
    </CustomLink>
  }
}

export default ProcessCard
