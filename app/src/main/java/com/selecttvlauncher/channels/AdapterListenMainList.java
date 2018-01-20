//package com.selecttvlauncher.channels;
//
///**
// * Created by Appsolute dev on 26-Sep-17.
// */
//
//public class AdapterListenMainList extends RecyclerView.Adapter<ListenMainList.DataObjectHolder> {
//    Context context;
//    int mlistPosition = 0;
//    int setChannelsCount = 0;
//    public ArrayList<ChannelScheduler> list_data = new ArrayList<>();
//    public ArrayList<ChannelScheduler> listAll = new ArrayList<>();
//    int limit = 10;
//
//    public ListenMainList(ArrayList<ChannelScheduler> list_data, Context context) {
//        this.context = context;
//        this.list_data = list_data;
////            this.listAll = list_data;
////            if (list_data.size() > limit) {
////                this.list_data.addAll(list_data.subList(0, limit));
////            } else {
////                this.list_data.addAll(list_data);
////            }
//        setChannelsCount = this.list_data.size();
//    }
//
//    public void addList(ArrayList<ChannelScheduler> moreListItems) {
//        this.list_data.addAll(moreListItems);
//        this.notifyItemInserted(this.list_data.size());
//        this.notifyDataSetChanged();
//    }
//
//    @Override
//    public ListenMainList.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        //LayoutInflater mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel_grid, parent, false);
//        return new DataObjectHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final ListenMainList.DataObjectHolder holder, final int position) {
//        if (position == 0)
//            holder.itemView.setBackgroundColor(Color.parseColor("#3e99f2"));
//        holder.fragment_ondemandlist_items.setText(list_data.get(position).getName());
//        try {
//            holder.fragment_ondemandlist_items.setTypeface(OpenSans_Regular);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
////                holder.imageView.loadImage(list_data.get(position).getLogo());
//            Image.loadImage(getActivity(), list_data.get(position).getLogo(), holder.imageView);
//            if (position == 0)
//                holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
//            else
//                holder.fragment_ondemandlist_items.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_light_grey));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        holder.fragment_ondemandlist_items_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                swaplist(position);
//            }
//        });
//
//        holder.fragment_ondemandlist_items_layout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                try {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        holder.fragment_ondemandlist_items_layout.setAlpha(0.5f);
//
//                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                        holder.fragment_ondemandlist_items_layout.setAlpha(1f);
//                    } else {
//                        holder.fragment_ondemandlist_items_layout.setAlpha(1f);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                return false;
//            }
//        });
//
//    }
//
//    public void swapItems(int itemAIndex) {
//        //make sure to check if dataset is null and if itemA and itemB are valid indexes.
//
//        try {
//            ChannelScheduler itemA = list_data.get(itemAIndex);
//            list_data.remove(itemAIndex);
//            list_data.add(0, itemA);
//
//            notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void setData(ArrayList<ChannelScheduler> data) {
//        //make sure to check if dataset is null and if itemA and itemB are valid indexes.
//        try {
//            this.list_data = data;
//            notifyDataSetChanged(); //This will trigger onBindViewHolder method from the adapter.
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list_data.size();
//    }
//
//    public class DataObjectHolder extends RecyclerView.ViewHolder {
//        LinearLayout fragment_ondemandlist_items_layout;
//        TextView fragment_ondemandlist_items;
//        DynamicImageView imageView;
//
//        public DataObjectHolder(View itemView) {
//            super(itemView);
//            fragment_ondemandlist_items_layout = (LinearLayout) itemView.findViewById(R.id.item_grid);
//            fragment_ondemandlist_items = (TextView) itemView.findViewById(R.id.txtChannelName);
//            imageView = (DynamicImageView) itemView.findViewById(R.id.imageShowThumbnail);
//        }
//    }
//
//    private void swaplist(int position) {
//        try {
//            Log.e("position", "::" + position);
//            ArrayList<ChannelScheduler> tempList = new ArrayList<>();
//            tempList.addAll(list_data);
//            ChannelScheduler cs = tempList.get(position);
////                notifyItemMoved(position, 0);
////                listenSubListt.notifyItemMoved(position, 0);
//            tempList.remove(position);
//            tempList.add(0, cs);
//            listenSubListt.swaplist(position);
//            setData(tempList);
////                highlightview("");
//            setPlayVideo(cs);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
//    public void updateChannelList() {
//        try {
//            int end = 0;
//            ArrayList<ChannelScheduler> filteredList = new ArrayList<>();
////                if (setChannelsCount < listAll.size()) {
////                    if (listAll.size() >= setChannelsCount + 2) {
////                        //end=setChannelsCount+2;
////                        filteredList.addAll(listAll.subList(setChannelsCount, setChannelsCount + 2));
////                    } else {
////                        end = listAll.size() - 1;
////                        filteredList.addAll(listAll.subList(setChannelsCount, listAll.size() - 1));
////                    }
////                    setChannelsCount += filteredList.size();
////                    addList(filteredList);
////                }
//            if (listAll.size() > 0)
//                if (this.list_data.size() < listAll.size()) {
//                    int offset = list_data.size() - 1;
////                        List<ChannelScheduler> temoList = listAll.subList(offset, 1);
//                    filteredList.add(listAll.get(offset + 1));
//                    addList(filteredList);
//                }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//}
