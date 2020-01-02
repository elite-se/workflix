// @flow

import React from 'react'
import type { ProcessType } from '../../datatypes/ProcessType'
import ProcessApi from '../../api/ProcessApi'
import withPromiseResolver from '../withPromiseResolver'
import ProcessCard from './ProcessCard'
import styled from 'styled-components'
import type { TaskType } from '../../datatypes/TaskType'
import TaskDrawer from './TaskDrawer'
import UserApi from '../../api/UsersApi'
import type { UserType } from '../../datatypes/models'

const ProcessListWrapper = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: row;
`

type PropsType = {|
  processes: Array<ProcessType>,
  users: Map<string, UserType>,
  path: string
|}

class TasksOverview extends React.Component<PropsType, { selectedTask: ?TaskType }> {
  state = { selectedTask: null }

  onTaskSelected = (selectedTask: TaskType) => {
    this.setState({ selectedTask: selectedTask })
  }

  onDrawerClosed = () => {
    this.setState({ selectedTask: null })
  }

  render () {
    return <div>
      <ProcessListWrapper>{
        this.props.processes.map(process => (
          <ProcessCard
            key={process.id}
            process={process}
            selectedTask={this.state.selectedTask}
            onTaskSelected={this.onTaskSelected}
            users={this.props.users} />)
        )
      }</ProcessListWrapper>
      <TaskDrawer
        selectedTask={this.state.selectedTask}
        onClose={this.onDrawerClosed}
        users={this.props.users} />
    </div>
  }
}

const promiseCreator = () => Promise.all([
  new ProcessApi().getProcesses(),
  new UserApi().getUsers()
]).then(
  ([processes, users]) => ({ processes, users })
)

export default withPromiseResolver<PropsType, {| processes: Array<ProcessType>, users: Map<string, UserType> |}>(promiseCreator)(TasksOverview)
