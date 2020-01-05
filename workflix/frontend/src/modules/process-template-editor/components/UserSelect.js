// @flow

import { Button, MenuItem } from '@blueprintjs/core'
import type { UserType } from '../../datatypes/User'
import type { ItemPredicate } from '@blueprintjs/select'
import { ItemRenderer, Select } from '@blueprintjs/select'
import highlightText from '../../common/highlightText'
import React from 'react'

const CustomSelect = Select.ofType<UserType>()

type PropsType = {
  users: UserType[],
  activeItem: ?UserType,
  onItemSelect: UserType => void
}

class UserSelect extends React.Component<PropsType> {
  itemRenderer: ItemRenderer = (item: UserType, { handleClick, modifiers, query }) => {
    return <MenuItem
      active={modifiers.active}
      disabled={modifiers.disabled}
      icon={this.props.activeItem === item ? 'tick' : 'blank'}
      label={highlightText(item.displayname, query)}
      key={item.id}
      onClick={handleClick}
      shouldDismissPopover={false}
      text={highlightText(item.name, query)}/>
  }

  filterUsers: ItemPredicate<UserType> = (query, user, _index, exactMatch) => {
    const normalizedName = user.name.toLocaleLowerCase()
    const normalizedDN = user.displayname.toLocaleLowerCase()
    const normalizedQuery = query.toLocaleLowerCase()
    return exactMatch
      ? [normalizedName, normalizedDN].includes(normalizedQuery)
      : (normalizedName + normalizedDN).indexOf(normalizedQuery) >= 0
  }

  render () {
    const { users, activeItem, onItemSelect } = this.props
    return <CustomSelect items={Array.from(users.values())}
                         activeItem={activeItem}
                         itemPredicate={this.filterUsers}
                         itemRenderer={this.itemRenderer}
                         onItemSelect={onItemSelect}>
      <Button icon='user'
              rightIcon='caret-down'
              fill
              text={activeItem?.name || '(No selection)'}/>
    </CustomSelect>
  }
}

export default UserSelect
