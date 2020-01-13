// @flow

import React from 'react'

const SCROLL_DELAY_MS = 300

class ScrollIntoViewOnMount extends React.Component<{ children: React$Node }> {
  containerDiv: ?HTMLDivElement = null

  ref = (elem: ?HTMLDivElement) => {
    this.containerDiv = elem
    setTimeout(() => this.containerDiv && this.containerDiv.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
      inline: 'center'
    }), SCROLL_DELAY_MS)
  }

  render () {
    return <div ref={this.ref} style={{ width: '100%' }}>{this.props.children}</div>
  }
}

export default ScrollIntoViewOnMount
