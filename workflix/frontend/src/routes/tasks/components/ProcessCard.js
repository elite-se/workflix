// @flow

import React from 'react'
import type { StyledComponent } from 'styled-components'
import styled from 'styled-components'
import { Button, Card, Collapse, H3 } from '@blueprintjs/core'
import type { ProcessType } from '../../../modules/datatypes/Process'
import TaskSummaryCard from './TaskSummaryCard'
import { Elevation } from '@blueprintjs/core/lib/cjs/common/elevation'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import type { UserType } from '../../../modules/datatypes/User'
import ProcessProgress from '../../../modules/common/components/ProcessProgress'
import { Link } from '@reach/router'
import calcTasksGraph from '../../../modules/process-template-editor/calcTasksGraph'
import stopPropagation from '../../../modules/common/stopPropagation'

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

type StateType = {
  expandDone: boolean,
  expandBlocked: boolean
}

const DISPLAY_TASKS = 2

class ProcessCard extends React.Component<PropsType, StateType> {
  state = {
    expandDone: false,
    expandBlocked: false
  }

  isSelected (task: TaskType): boolean {
    const selectedTask = this.props.selectedTask
    return selectedTask != null && task.id === selectedTask.id
  }

  switchExpandDone = stopPropagation(() => this.setState(state => ({ expandDone: !state.expandDone })))
  switchExpandBlocked = stopPropagation(() => this.setState(state => ({ expandBlocked: !state.expandBlocked })))

  renderNodes (nodes: *): React$Node {
    const { onTaskSelected, users, process, taskTemplates } = this.props
    return nodes.map(node => <TaskSummaryCard key={node.id} task={node.data.task}
                                              selected={this.isSelected(node.data.task)}
                                              processGroupId={process.processGroupId}
                                              onTaskSelected={onTaskSelected} users={users}
                                              taskTemplates={taskTemplates}/>)
  }

  render () {
    const { process, taskTemplates } = this.props
    const { expandDone, expandBlocked } = this.state
    const isSelected = !!process.tasks.find(task => this.isSelected(task))
    const processedNodes = calcTasksGraph(process.tasks, taskTemplates)
    const closedNodes = processedNodes.filter(task => task.data.task.status === 'CLOSED')
    const runningNodes = processedNodes.filter(task => task.data.task.status === 'RUNNING')
    const blockedNodes = processedNodes.filter(task => task.data.task.status === 'BLOCKED')
    const nonCollapsing = closedNodes.slice(closedNodes.length - DISPLAY_TASKS)
      .concat(runningNodes)
      .concat(blockedNodes.slice(0, DISPLAY_TASKS))
    const collapseClosed = closedNodes.slice(0, closedNodes.length - DISPLAY_TASKS)
    const collapseBlocked = blockedNodes.slice(DISPLAY_TASKS)
    return <CustomLink to={`/processes/${process.id}`}>
      <CustomCard interactive elevation={isSelected ? Elevation.FOUR : undefined}>
        <H3>{process.title} (#{process.id})</H3>
        <TaskList>
          {
            collapseClosed.length > 0 && <>
              <Button minimal fill onClick={this.switchExpandDone}>
                {expandDone ? 'Collapse' : 'Expand'} {collapseClosed.length} finished {collapseClosed.length > 1 ? 'Tasks' : 'Task'}
              </Button>
              <Collapse isOpen={expandDone}>
                {this.renderNodes(collapseClosed)}
              </Collapse>
            </>
          }
          {this.renderNodes(nonCollapsing)}
          {
            collapseBlocked.length > 0 && <>
              <Button minimal fill onClick={this.switchExpandBlocked}>
                {expandBlocked ? 'Collapse' : 'Expand'} {collapseBlocked.length} blocked {collapseBlocked.length > 1 ? 'Tasks' : 'Task'}
              </Button>
              <Collapse isOpen={expandBlocked}>
                {this.renderNodes(collapseBlocked)}
              </Collapse>
            </>
          }
        </TaskList>
        <ProcessProgress process={process}/>
      </CustomCard>
    </CustomLink>
  }
}

export default ProcessCard
