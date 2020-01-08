// @flow

import React from 'react'
import { Icon } from '@blueprintjs/core'
import styled from 'styled-components'
import { IIconProps } from '@blueprintjs/core/lib/cjs/components/icon/icon'

const Item = styled<{}, {}, *>('div')`
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  & > * {
    padding: 5px;
  }
`

export default class IconRow extends React.Component<IIconProps & { children: React$Node, singleLine: boolean }> {
  static defaultProps = { singleLine: false }

  render () {
    return <Item style={{ alignItems: this.props.singleLine ? 'center' : 'flex-start' }}>
      <Icon {...this.props}/>
      <div>{this.props.children}</div>
    </Item>
  }
}
