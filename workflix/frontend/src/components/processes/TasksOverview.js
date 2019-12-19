// @flow

import React from 'react'
import type { ProcessType } from '../../datatypes/ProcessType'
import ProcessApi from '../../api/ProcessApi'
import withPromiseResolver from '../withPromiseResolver'
import ProcessCard from './ProcessCard'
import styled from 'styled-components'
import { Drawer } from '@blueprintjs/core'

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

  onDrawerClosed = () => {
    this.setState({ selectedTaskId: null })
  }

  // pretty hacky: "remove" the class of the Blueprint.js overlay between portal and drawer that prevents clicks in
  // the rest of the page
  onDrawerOpening = (elem: HTMLElement) => {
    const overlayElem = elem.parentElement
    if (overlayElem == null) { return }
    overlayElem.classList.remove('bp3-overlay-container')
  }

  render () {
    const selectedTaskId = this.state.selectedTaskId
    return <div>
      <ProcessListWrapper>{
        this.props.processes.map(process => (
          <ProcessCard
            key={process.id}
            process={process}
            selectedTaskId={this.state.selectedTaskId}
            onTaskSelected={this.onTaskSelected} />)
        )
      }</ProcessListWrapper>
      <Drawer
        size={Drawer.SIZE_SMALL}
        hasBackdrop={false}
        isOpen={selectedTaskId != null}
        title={`Task ${selectedTaskId != null ? selectedTaskId : ''}`}
        onClose={this.onDrawerClosed}
        onOpening={this.onDrawerOpening}>
        The selected task id: {selectedTaskId}
      </Drawer>
    </div>
  }
}

const promiseCreator = () => new ProcessApi().getProcesses().then(
  processes => ({ processes })
)

export default withPromiseResolver<PropsType, {| processes: Array<ProcessType> |}>(promiseCreator)(TasksOverview)
