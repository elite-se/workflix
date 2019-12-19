// @flow

import React from 'react'
import { H2, Spinner } from '@blueprintjs/core'
import styled from 'styled-components'
import type { ProcessType } from '../../datatypes/ProcessType'
import ProcessList from './ProcessList'
import ProcessApi from '../../api/ProcessApi'

const CenterScreen = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;
`

class TasksOverview extends React.Component<{}, { processes: ?ProcessType[] } > {
  state = { processes: null }
  componentDidMount () {
    new ProcessApi().getProcesses().then(
      processes => this.setState({ processes })
    )
  }

  render () {
    if (!this.state.processes) {
      return <CenterScreen>
        <Spinner />
      </CenterScreen>
    } else {
      return <div>
        <H2 style={{ textAlign: 'center' }}>All processes</H2>
        <ProcessList processes={this.state.processes} />
      </div>
    }
  }
}

export default TasksOverview
