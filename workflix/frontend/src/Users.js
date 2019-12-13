// @flow

import React from 'react'
import { Card, H2, H3, Text } from '@blueprintjs/core'
import type { UserType } from './models'
import withPromiseResolver from './withPromiseResolver'

class Users extends React.Component<{ users: Array<UserType> }> {
  render () {
    return <div style={{
      margin: '20px',
      display: 'flex',
      maxWidth: '300px',
      flex: '1',
      justifyContent: 'flex-start',
      alignItems: 'stretch',
      flexDirection: 'column'
    }}>
      <H2 style={{ textAlign: 'center' }}>All Users</H2>
      {
        this.props.users.map(user => (
          <Card key={user.id} style={{ margin: '5px' }}>
            <H3>{user.displayname}</H3>
            <Text>{user.name}</Text>
            <a href={`mailto:${user.email}`}>{user.email}</a>
          </Card>)
        )
      }
    </div>
  }
}

export default withPromiseResolver(
  () => fetch('https://wf-backend.herokuapp.com/users')
    .then(response => response.json())
    .then(users => ({ users }))
)(Users)
