// @flow

import React from 'react'
import Header from './Header'
import styled from 'styled-components'
import MainRouter from './MainRouter'
import { getToken } from './modules/common/tokenStorage'
import Login from './routes/login/components/Login'

const RouterWrapper = styled<{}, {}, 'div'>('div')`
  &, & > * {
    margin: 5px;
    display: flex;
    flex: 1;
    justify-content: center;
    max-width: 100%;
  }
`

class App extends React.Component<{}, { loggedIn: boolean }> {
  state = { loggedIn: !!getToken() }

  onLoggedInChanged = (loggedIn: boolean) => this.setState({ loggedIn })

  render () {
    return this.state.loggedIn
      ? <div style={{ display: 'flex', flexDirection: 'column', flex: 1, maxWidth: '100%' }}>
          <Header/>
          <RouterWrapper><MainRouter onLoggedInChanged={this.onLoggedInChanged}/></RouterWrapper>
        </div>
      : <Login onLoggedInChanged={this.onLoggedInChanged}/>
  }
}

export default App
