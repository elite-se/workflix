// @flow

import React from 'react'
import Header from './Header'
import styled from 'styled-components'
import MainRouter from './MainRouter'

const RouterWrapper = styled<{}, {}, 'div'>('div')`
  &, & > * {
    margin: 20px;
    display: flex;
    flex: 1;
    justify-content: center;
  }
`

class App extends React.Component<{}> {
  render () {
    return <div style={{ display: 'flex', flexDirection: 'column', flex: 1 }}>
      <Header />
      <RouterWrapper><MainRouter /></RouterWrapper>
    </div>
  }
}

export default App
