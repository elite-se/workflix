// @flow

import React from 'react'
import { IMultiSelectProps, ItemListPredicate, ItemRenderer, MultiSelect } from '@blueprintjs/select'
import { MenuItem } from '@blueprintjs/core'
import { filter, sortBy, uniq, without } from 'lodash'
import highlightText from '../highlightText'

type PropsType<T, IdType: number | string> = {|
  items: T[],
  selection: T[],
  onSelectionChanged: (selection: T[]) => void,
  render: (T) => string,
  toID: (T) => IdType,
  multiSelectProps?: IMultiSelectProps
|}

class SimpleMultiSelect<T, IdType: number | string> extends React.Component<PropsType<T, IdType>> {
  renderItem: ItemRenderer<T> = (item, { modifiers, handleClick, query }) => {
    if (!modifiers.matchesPredicate) {
      return null
    }
    return <MenuItem
      active={modifiers.active}
      icon={this.isSelected(item) ? 'tick' : 'blank'}
      key={this.props.toID(item)}
      onClick={handleClick}
      text={highlightText(this.props.render(item), query)}
      shouldDismissPopover={false}
    />
  }

  isSelected: (T => boolean) = item => this.props.selection.includes(item)

  onItemSelect = (item: T) =>
    this.props.onSelectionChanged(
      this.isSelected(item)
        ? without(this.props.selection, item)
        : uniq([...this.props.selection, item]))

  onTagRemove = (tag: string, index: number) => {
    this.props.onSelectionChanged(this.props.selection.filter((_, idx) => idx !== index))
  }

  itemListPredicate: ItemListPredicate<T> = (query, items) => {
    const queryLower = query.toLocaleLowerCase()
    const render = this.props.render
    return sortBy(filter(items, item => render(item).toLocaleLowerCase().includes(queryLower)),
      item => !render(item).toLocaleLowerCase().startsWith(queryLower),
      item => render(item).toLocaleLowerCase()
    )
  }

  render () {
    const BlueprintProcessGroupMultiSelect = MultiSelect.ofType<T>()
    return <BlueprintProcessGroupMultiSelect
      {...this.props.multiSelectProps}
      itemListPredicate={this.itemListPredicate}
      itemRenderer={this.renderItem}
      items={this.props.items}
      noResults={<MenuItem icon='disable' text='No results.' disabled/>}
      onItemSelect={this.onItemSelect}
      tagRenderer={this.props.render}
      tagInputProps={{ onRemove: this.onTagRemove }}
      selectedItems={this.props.selection}/>
  }
}

export default SimpleMultiSelect
