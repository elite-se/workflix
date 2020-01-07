// @flow

import { Card, H3, UL } from '@blueprintjs/core'
import React from 'react'
import type { UserRoleType, UserType } from '../../../modules/datatypes/User'
import type { ProcessGroupType } from '../../../modules/datatypes/ProcessGroup'
import { isEmpty, sortBy } from 'lodash'
import IconRow from './IconRow'

type PropsType = {|
  user: UserType,
  processGroups: Map<number, ProcessGroupType>,
  roles: Map<number, UserRoleType>
|}

function listIfNeeded <T> (values: T[], keySupply: T => number, renderer: T => React$Node): React$Node {
  if (isEmpty(values)) {
    return '–'
  } else if (values.length === 1) {
    return renderer(values[0])
  } else {
    // eslint-disable-next-line react/jsx-pascal-case
    return <UL style={{ listStylePosition: 'inside', padding: 0, margin: 0 }}>
      {values.map(value => <li key={keySupply(value)}>{renderer(value)}</li>)}
    </UL>
  }
}

class UserCard extends React.Component<PropsType> {
  render () {
    const { user, processGroups, roles } = this.props
    const usersGroups = sortBy(user.processGroupIds.map(id => processGroups.get(id)).filter(Boolean),
      group => group.title)
    const usersRoles = sortBy(user.userRoleIds.map(id => roles.get(id)).filter(Boolean),
      role => role.name)
    console.debug(user)
    return <Card key={user.id} style={{ margin: '5px' }}>
      <H3>{user.name}</H3>
        <IconRow icon='person'>{user.displayname}</IconRow>
        <IconRow icon='envelope'><a href={`mailto:${user.email}`}>{user.email}</a></IconRow>
        <IconRow icon='office'>
          {listIfNeeded(usersGroups, group => group.id, group => group.title)}
        </IconRow>
        <IconRow icon='badge'>
          {listIfNeeded(usersRoles, role => role.id, role => role.name)}
        </IconRow>
    </Card>
  }
}

export default UserCard
