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

class ProcessList extends React.Component<{ processes: Array<ProcessType> }, {}> {
  render () {
    return <ProcessListWrapper>{
      this.props.processes.map(process => (
        <ProcessCard key={process.id} process={process} />)
      )
    }</ProcessListWrapper>
  }
}
export default ProcessList
