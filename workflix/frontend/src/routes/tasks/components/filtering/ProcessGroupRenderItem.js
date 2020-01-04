// @flow

import React from 'react'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'

export class ProcessGroupRenderItem extends React.Component<{procGroup: ProcessGroupType}> {
  render = () => this.props.procGroup.title
}

export default ProcessGroupRenderItem
