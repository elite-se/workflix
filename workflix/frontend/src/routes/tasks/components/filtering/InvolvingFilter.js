// @flow

import React from 'react'
import type { UserType } from '../../../../modules/datatypes/User'
import UserSelect from '../../../../modules/common/UserSelect'
import type { FiltersType } from '../../../../modules/datatypes/Filters'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void,
  users: Map<string, UserType>
|}

class StateFilter extends React.Component<PropsType> {
  onUserSelected = (user: ?UserType) => {
    this.props.onFiltersChanged({
      ...this.props.filters,
      involving: user
    })
  }

  render () {
    const selectedUser = this.props.filters.involving
    return <UserSelect
      users={Array.from(this.props.users.values())}
      activeItem={selectedUser}
      onItemSelect={this.onUserSelected}
      textPrefix='Involving: '
      nullSelectionText='any'
      allowNullSelection/>
  }
}

export default StateFilter
