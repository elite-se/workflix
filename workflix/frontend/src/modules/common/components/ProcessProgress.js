// @flow

import React from 'react'
import styled from 'styled-components'
import { ProgressBar, Tooltip } from '@blueprintjs/core'
import type { ProcessType } from '../../datatypes/Process'
import { statusTranslation } from '../../datatypes/Process'
import { Intent } from '@blueprintjs/core/lib/cjs/common/intent'

const HUNDRED = 100

const CustomProgressBar = styled(ProgressBar)`
  margin: 10px 0;
  min-width: 200px;
`

class ProcessProgress extends React.Component<{ process: ProcessType }> {
  render () {
    const process = this.props.process
    const [progressIntent, progressValue] =
      process.status === 'ABORTED'
        ? [Intent.DANGER, 1]
        : process.status === 'CLOSED'
          ? [Intent.SUCCESS, 1]
          : [Intent.PRIMARY, process.progress / HUNDRED]
    return <Tooltip content={`This process is ${statusTranslation[process.status]}.`} fill>
      <CustomProgressBar animate={false} intent={progressIntent} value={progressValue}/>
    </Tooltip>
  }
}

export default ProcessProgress
