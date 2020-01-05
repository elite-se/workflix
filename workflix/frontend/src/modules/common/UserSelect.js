// @flow

import { Button, Intent, MenuItem } from '@blueprintjs/core'
import type { ItemPredicate } from '@blueprintjs/select'
import { ItemRenderer, Select } from '@blueprintjs/select'
import React from 'react'
import highlightText from './highlightText'
import type { UserType } from '../datatypes/User'

const CustomSelect = Select.ofType<?UserType>()

type PropsType = {
  users: UserType[],
  activeItem: ?UserType,
  onItemSelect: ?UserType => void,
  nullSelectionText?: string,
  allowNullSelection?: boolean,
  textPrefix?: string,
  fill?: boolean,
  intent?: Intent
}

class UserSelect extends React.Component<PropsType> {
  itemRenderer: ItemRenderer = (item: ?UserType, { handleClick, modifiers, query }) => {
    return <MenuItem
      active={modifiers.active}
      disabled={modifiers.disabled}
      icon={this.props.activeItem === item ? 'tick' : 'blank'}
      label={highlightText(item?.displayname || '', query)}
      key={item?.id || ''}
      onClick={handleClick}
      shouldDismissPopover={false}
      text={highlightText(this.getNameOrNoSelectionText(item), query)}/>
  }

  filterUsers: ItemPredicate<?UserType> = (query, user, _index, exactMatch) => {
    const normalizedName = this.getNameOrNoSelectionText(user).toLocaleLowerCase()
    const normalizedDN = user ? user.displayname.toLocaleLowerCase() : ''
    const normalizedQuery = query.toLocaleLowerCase()
    return exactMatch
      ? [normalizedName, normalizedDN].includes(normalizedQuery)
      : (normalizedName + normalizedDN).indexOf(normalizedQuery) >= 0
  }

  render () {
    const { users, activeItem, onItemSelect, fill } = this.props
    const items = this.props.allowNullSelection ? [null, ...users] : users
    return <CustomSelect items={items}
                         popoverProps={{ fill }}
                         itemPredicate={this.filterUsers}
                         itemRenderer={this.itemRenderer}
                         onItemSelect={onItemSelect}>
      <Button icon='user'
              rightIcon='caret-down'
              fill
              intent={this.props.intent}
              text={`${this.props.textPrefix || ''}
              ${this.getNameOrNoSelectionText(activeItem)}`}/>
    </CustomSelect>
  }

  getNameOrNoSelectionText (user: ?UserType): string {
    return user?.name || this.props.nullSelectionText || '(No selection)'
  }
}

export default UserSelect
