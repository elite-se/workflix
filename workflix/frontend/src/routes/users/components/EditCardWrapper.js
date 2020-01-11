// @flow

import React from 'react'
import styled from 'styled-components'
import OutsideClickHandler from 'react-outside-click-handler'
import ScrollIntoViewOnMount from '../../../modules/common/components/ScrollIntoViewOnMount'

const StyleWrapper = styled<{}, {}, *>('div')`
  display: flex;
  flex-direction: column;
  & > div {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
  }
  
  & > div > div {
    flex-grow: 1;
    display: flex;
    flex-direction: column;
  }
  
  & > div > div > div {
    flex-grow: 1;
  }
`

export default class EditCardWrapper extends React.Component<{ children: React$Node, onDeselect: () => void }> {
  render () {
    return <StyleWrapper>
      <OutsideClickHandler onOutsideClick={this.props.onDeselect}>
        <ScrollIntoViewOnMount>
          {this.props.children}
        </ScrollIntoViewOnMount>
      </OutsideClickHandler>
    </StyleWrapper>
  }
}
