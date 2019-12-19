// @flow

import React from 'react'
import type { ProcessType } from '../../datatypes/ProcessType'
import ProcessApi from '../../api/ProcessApi'
import withPromiseResolver from '../withPromiseResolver'
import ProcessCard from './ProcessCard'
import styled from 'styled-components'

const ProcessListWrapper = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: row;
`

type PropsType = {| processes: Array<ProcessType>, path: string |}

class TasksOverview extends React.Component<PropsType, { selectedTaskId: ?number }> {
  state = { selectedTaskId: null }

  onTaskSelected = (selectedTaskId: number) => {
    this.setState({ selectedTaskId: selectedTaskId })
  }

  render () {
    return <div><ProcessListWrapper>{
      this.props.processes.map(process => (
        <ProcessCard
          key={process.id}
          process={process}
          selectedTaskId={this.state.selectedTaskId}
          onTaskSelected={this.onTaskSelected} />)
      )
    }</ProcessListWrapper></div>
  }
}

const promiseCreator = () => new ProcessApi().getProcesses().then(
  processes => ({ processes })
)

export default withPromiseResolver<PropsType, {| processes: Array<ProcessType> |}>(promiseCreator)(TasksOverview)
