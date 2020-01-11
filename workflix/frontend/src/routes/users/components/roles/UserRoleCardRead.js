// @flow

import React from 'react'
import type { UserRoleType, UserType } from '../../../../modules/datatypes/User'
import { sortBy } from 'lodash'
import IconRow from '../IconRow'
import listIfNeeded from '../../listIfNeeded'
import StyledCard from '../StyledCard'
import stopPropagation from '../../../../modules/common/stopPropagation'
import { H3 } from '@blueprintjs/core'

type PropsType = {|
  userRole: UserRoleType,
  users: Map<string, UserType>,
  onUserSelected: (UserType) => void,
  onRoleSelected: (?UserRoleType) => void
|}

class UserRoleCardRead extends React.Component<PropsType> {
  onUserSelected = (user: UserType) => stopPropagation(() => this.props.onUserSelected(user))
  onClick = () => this.props.onRoleSelected(this.props.userRole)

  render () {
    const { userRole, users } = this.props
    const roleUsers = sortBy(userRole.memberIds.map(id => users.get(id)).filter(Boolean),
      user => user.name)
    return <StyledCard key={userRole.id} onClick={this.onClick} interactive>
      <IconRow icon='people'><H3>{userRole.name}</H3></IconRow>
      {userRole.description && <IconRow icon='annotation' multiLine>
        <span style={{ whiteSpace: 'pre-wrap' }}>{userRole.description}</span>
      </IconRow>}
      <IconRow icon='person' multiLine>
        {listIfNeeded(roleUsers, user => user.id,
          user => <a onClick={this.onUserSelected(user)}>{user.name}</a>)}
      </IconRow>
    </StyledCard>
  }
}

export default UserRoleCardRead
