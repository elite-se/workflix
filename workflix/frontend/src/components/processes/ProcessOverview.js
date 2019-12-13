// @flow

import React from 'react'
import { H2, Spinner } from '@blueprintjs/core'
import styled from 'styled-components'
import type ProcessType from '../../datatypes/ProcessType'
import ProcessList from './ProcessList'

const CenterScreen = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;
`

class ProcessOverview extends React.Component<{}, { processes: ?Array<ProcessType> }> {
  state = {
    processes: [{
      masterData: {
        id: 1,
        title: 'Process 1'
      },
      tasks: [
        {
          id: 1,
          name: 'Task 1',
          finished: true
        },
        {
          id: 2,
          name: 'Task 2',
          finished: true
        },
        {
          id: 3,
          name: 'Task 3',
          finished: true
        },
        {
          id: 4,
          name: 'Task 4'
        }
      ]
    }, {
      masterData: {
        id: 2,
        title: 'Process 2'
      },
      tasks: [
        {
          id: 5,
          name: 'Task 5',
          finished: true
        },
        {
          id: 6,
          name: 'Task 6'
        }
      ]
    }]
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

export default ProcessOverview
