// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import TitledCard from '../TitledCard'

type PropsType = {|
  userRole: UserRoleType,
  users: Map<string, UserType>,
  onUserSelected: (UserType) => void
|}

class UserRoleCard extends React.Component<PropsType> {
  onUserSelected = (user: UserType) => () => this.props.onUserSelected(user)

  render () {
    const { userRole, users } = this.props
    const roleUsers = sortBy(userRole.memberIds.map(id => users.get(id)).filter(Boolean),
      user => user.name)
    return <TitledCard key={userRole.id} title={<IconRow icon='badge' singleLine>{userRole.name}</IconRow>}>
      <IconRow icon='person' multiLine>
        {listIfNeeded(roleUsers, user => user.id,
          user => <a onClick={this.onUserSelected(user)}>{user.name}</a>)}
      </IconRow>
    </TitledCard>
  }
}

export default UserRoleCard
