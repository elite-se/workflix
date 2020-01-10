// @flow

import React from 'react'
import type { ProcessType } from '../../../modules/datatypes/Process'
import ProcessApi from '../../../modules/api/ProcessApi'
import ProcessCard from './ProcessCard'
import styled from 'styled-components'
import type { TaskTemplateType, TaskType } from '../../../modules/datatypes/Task'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import withPromiseResolver from '../../../modules/app/hocs/withPromiseResolver'
import TaskDrawer from './drawer/TaskDrawer'
import type { FiltersType } from '../../../modules/datatypes/Filters'
import { Colors } from '@blueprintjs/core'
import { isEmpty } from 'lodash'

const ProcessListWrapper = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: row;
  align-items: flex-start;
`

type PropsType = {|
  filters: FiltersType,
  processes: Array<ProcessType>,
  taskTemplates: Map<number, TaskTemplateType>,
  users: Map<string, UserType>,
  onTaskSelected: ?TaskType => void,
  refresh: (soft: boolean) => void,
  userRoles: Map<number, UserRoleType>
|}

type StateType = {|
  selectedTaskId: ?number
|}

class ProcessList extends React.Component<PropsType, StateType> {
  state = {
    selectedTaskId: null
  }

  onTaskSelected = (selectedTask: ?TaskType) => {
    this.setState({ selectedTaskId: selectedTask?.id })
    this.props.onTaskSelected(selectedTask)
  }

  onDrawerClosed = () => {
    this.onTaskSelected(null)
  }

  onTaskModified = () => {
    this.props.refresh(true)
  }

  findSelectedTask = () =>
    this.props.processes
      .flatMap(process => process.tasks)
      .find(task => task.id === this.state.selectedTaskId)

  render () {
    const { processes, users, userRoles, taskTemplates } = this.props
    const selectedTask = this.findSelectedTask()
    return <div style={{
      maxWidth: '100%',
      overflowX: 'auto',
      display: 'flex',
      flex: 1
    }}>
      <ProcessListWrapper>
        {
          isEmpty(processes)
            ? <span style={{
              color: Colors.GRAY2,
              alignItem: 'stretch'
            }}>There are no processes matching the filters.</span>
            : processes.map(process => (
              <ProcessCard
                key={process.id}
                process={process}
                selectedTask={selectedTask}
                onTaskSelected={this.onTaskSelected}
                users={users}
                taskTemplates={taskTemplates}/>)
            )
        }</ProcessListWrapper>
      <TaskDrawer
        selectedTask={selectedTask}
        onClose={this.onDrawerClosed}
        onTaskModified={this.onTaskModified}
        users={users}
        userRoles={userRoles}
        taskTemplates={taskTemplates}/>
    </div>
  }
}

const promiseCreator = (props: *, refresh) =>
  new ProcessApi().getProcesses(props.filters)
    .then(processes => Promise.all([
      Promise.resolve(processes),
      new ProcessApi().getTaskTemplatesForProcessTemplates(processes.map(proc => proc.processTemplateId))
    ]))
    .then(
      ([processes, taskTemplates]) => ({
        processes,
        taskTemplates,
        refresh
      })
    )

const shouldUpdate = (oldProps: *, newProps: *) => oldProps.filters !== newProps.filters

export default withPromiseResolver<PropsType, *>(promiseCreator, shouldUpdate)(ProcessList)
