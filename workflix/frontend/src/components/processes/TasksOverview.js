// @flow

import React from 'react'
import type { ProcessType } from '../../datatypes/ProcessType'
import ProcessApi from '../../api/ProcessApi'
import withPromiseResolver from '../withPromiseResolver'
import ProcessCard from './ProcessCard'
import styled from 'styled-components'
import type { TaskType } from '../../datatypes/TaskType'
import TaskDrawer from './TaskDrawer'

const ProcessListWrapper = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: row;
`

type PropsType = {| processes: Array<ProcessType>, path: string |}

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
            onTaskSelected={this.onTaskSelected} />)
        )
      }</ProcessListWrapper>
      <TaskDrawer
        selectedTask={this.state.selectedTask}
        onClose={this.onDrawerClosed} />
    </div>
  }
}

const promiseCreator = () => new ProcessApi().getProcesses().then(
  processes => ({ processes })
)

export default withPromiseResolver<PropsType, {| processes: Array<ProcessType> |}>(promiseCreator)(TasksOverview)
