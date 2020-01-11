// @flow

import React from 'react'
import { Icon } from '@blueprintjs/core'
import styled from 'styled-components'
import { IIconProps } from '@blueprintjs/core/lib/cjs/components/icon/icon'

const Item = styled<{}, {}, *>('div')`
  display: flex;
  flex-direction: row;
  & > * {
    padding: 5px;
  }
`

export default class IconRow extends React.Component<IIconProps & { children: React$Node, multiLine: boolean }> {
  static defaultProps = { multiLine: true }

  render () {
    const { children, multiLine, ...iconProps } = this.props
    return <Item style={{ alignItems: this.props.multiLine ? 'flex-start' : 'center' }}>
      <Icon {...iconProps}/>
      <div style={{ width: '100%' }}>{this.props.children}</div>
    </Item>
  }
}
