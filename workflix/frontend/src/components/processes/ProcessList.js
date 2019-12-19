// @flow

import React from 'react'
import type { ProcessType } from '../../datatypes/ProcessType'
import styled from 'styled-components'
import ProcessCard from './ProcessCard'

const ProcessListWrapper = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  flex-direction: row;
`

class ProcessList extends React.Component<{ processes: Array<ProcessType> }, { selectedTaskId: ?number }> {
  state = { selectedTaskId: null }

  onTaskSelected = (selectedTaskId: number) => {
    this.setState({ selectedTaskId: selectedTaskId })
  }

  render () {
    return <ProcessListWrapper>{
      this.props.processes.map(process => (
        <ProcessCard
          key={process.id}
          process={process}
          selectedTaskId={this.state.selectedTaskId}
          onTaskSelected={this.onTaskSelected} />)
      )
    }</ProcessListWrapper>
  }
}
export default ProcessList
