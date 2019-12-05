// @flow

import React from 'react'
import { Card, H2, H3, Spinner, Text } from '@blueprintjs/core'
import styled from 'styled-components'

type UserType = {
  displayname: string,
  name: string,
  id: string,
  email: string
}

const CenterScreen = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex: 1;
  justify-content: center;
  align-items: center;
`

class Users extends React.Component<{}, { users: ?Array<UserType> }> {
  state = { users: null }

  componentDidMount () {
    fetch('https://wf-backend.herokuapp.com/users')
      .then(response => response.json())
      .then((users: Array<UserType>) => this.setState({ users }))
  }

  render () {
    if (!this.state.users) {
      return <CenterScreen>
        <Spinner />
      </CenterScreen>
    } else {
      return <div style={{
        margin: '20px',
        display: 'flex',
        width: '300px',
        flex: '1',
        justifyContent: 'flex-start',
        alignItems: 'stretch',
        flexDirection: 'column'
      }}>
        <H2 style={{ textAlign: 'center' }}>All Users</H2>
        {
          this.state.users.map(user => (
            <Card key={user.id} style={{
              margin: '5px',
              width: ''
            }}>
              <H3>{user.displayname}</H3>
              <Text>{user.name}</Text>
              <a href={`mailto:${user.email}`}>{user.email}</a>
            </Card>)
          )
        }
      </div>
    }
  }
}

export default Users
